package io.moyada.metadata.enhancement.proxy;

import io.moyada.metadata.enhancement.Proxy;
import io.moyada.metadata.enhancement.exception.ProxyException;
import io.moyada.metadata.enhancement.statement.BodyStatement;
import io.moyada.metadata.enhancement.statement.EmptyStatement;
import io.moyada.metadata.enhancement.support.Annotation;
import io.moyada.metadata.enhancement.support.Assert;
import io.moyada.metadata.enhancement.support.Value;
import io.moyada.metadata.enhancement.util.*;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.MemberValue;

import java.util.*;

/**
 * javassist 代理
 * @author xueyikang
 * @since 0.0.1
 **/
public abstract class JavassistProxy<T> implements Proxy<T> {

    private static final CtClass[] EMPTY_PARAM = new CtClass[0];

    private Class<?> clazz;

    CtClass originClass;
    CtClass targetClass;

    private Set<String> importPackage;

    public JavassistProxy(Class<T> targetClass) {
        this.clazz = targetClass;
        try {
            this.originClass = getOrigin(targetClass);
            this.targetClass = buildTarget(targetClass);
        } catch (NotFoundException e) {
            throw new ProxyException(clazz.getName() + " not found.", e);
        } catch (CannotCompileException e) {
            throw new ProxyException("can not fetch target class: " + clazz.getName(), e);
        }

        this.importPackage = new HashSet<>();

        Iterator<String> importedPackages = this.targetClass.getClassPool().getImportedPackages();
        while (importedPackages.hasNext()) {
            String pack = importedPackages.next();
            if (pack.equals("java.lang")) {
                continue;
            }
            this.importPackage.add(pack);
        }
    }

    private CtClass getOrigin(Class<T> target) throws NotFoundException, CannotCompileException {
        ClassPool classPool = ClassPool.getDefault();
        return classPool.getCtClass(target.getName());
    }

    abstract CtClass buildTarget(Class<T> target) throws NotFoundException, CannotCompileException;

    protected boolean isVoid(CtMethod method) {
        String returnType;
        try {
            returnType = method.getReturnType().getSimpleName();
        } catch (NotFoundException e) {
            return true;
        }

        return returnType.equalsIgnoreCase("void");
    }

    @Override
    public Proxy<T> addImport(String name) {
        Assert.checkNotNull(name, "class name");
        addPackage(name);
        return this;
    }

    @Override
    public Proxy<T> addField(String name, Class<?> type, int modifier) {
        return addField(name, type, modifier, new Annotation[0]);
    }

    @Override
    public Proxy<T> addField(String name, Class<?> type, int modifier, Value init) {
        return addField(name, type, modifier, init, new Annotation[0]);
    }

    @Override
    public Proxy<T> addField(String name, Class<?> type, int modifier, Annotation... annotations) {
        return addField(name, type, modifier, null, annotations);
    }

    @Override
    public Proxy<T> addField(String name, Class<?> type, int modifier, Value init, Annotation... annotations) {
        NameUtil.check(name);
        Assert.checkNotNull(type, "type");

        String statement = makeStatement(type, name, modifier, init);
        CtField ctField = makeField(statement, type, name, modifier);

        addAnnotation(ctField, annotations);

        try {
            targetClass.addField(ctField);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    private CtClass[] toClass(List<Class<?>> classes) {
        if (null == classes) {
            return new CtClass[0];
        }
        int size = classes.size();
        if (size == 0) {
            return new CtClass[0];
        }

        CtClass[] ctClasses = new CtClass[size];
        int index = 0;
        for (Class<?> clazz : classes) {
            ctClasses[index++] = toClass(clazz);
        }
        return ctClasses;
    }

    private CtClass toClass(Class<?> classType) {
        if (classType.isArray()) {
            classType = classType.getComponentType();
        }

        String className = classType.getName();
        addPackage(className);
        try {
            return ClassPool.getDefault().getCtClass(className);
        } catch (NotFoundException e) {
            throw new ProxyException(e);
        }
    }

    private void addPackage(String className) {
        if (TypeUtil.isBaseType(className) || importPackage.contains(className)) {
            return;
        }
        targetClass.getClassPool().importPackage(className);
        importPackage.add(className);
    }

    private String makeStatement(Class<?> classType, String name, int modifier, Value init) {
        StringBuilder statement = new StringBuilder(ModifierUtil.valueOf(modifier));
        statement.append(classType.getSimpleName());
        statement.append(" ");
        statement.append(name);

        if (null != init) {
            Class<?> type = init.getType();
            if (null != type) {
                addPackage(type.getName());
            }
            statement.append(" = ");
            statement.append(init.getStatement());
        }

        return statement.append(";").toString();
    }

    private CtField makeField(String statement, Class<?> classType, String name, int modifier) {
        CtClass ctClass = toClass(classType);

        CtField ctField;
        try {
            ctField = CtField.make(statement, targetClass);
        } catch (CannotCompileException e) {
            throw new ProxyException(e);
        }

        ctField.setModifiers(modifier);
        ctField.setType(ctClass);
        ctField.setName(name);

        return ctField;
    }

    @Override
    public Proxy<T> addMethod(String name, List<Class<?>> paramType, Class<?> returnType, List<Class<? extends Throwable>> exception,
                              int modifier, BodyStatement body, Annotation... annotations) {
        NameUtil.check(name);
        if (null == returnType) {
            returnType = void.class;
        }
        if (null == body) {
            body = EmptyStatement.INSTANCE;
        }

        CtClass returnClass = toClass(returnType);
        CtClass[] paramClass = toClass(paramType);
        CtClass[] exceClass;
        if (null == exception) {
            exceClass = new CtClass[0];
        } else {
            List<Class<?>> exceptions = new ArrayList<>();
            exceptions.addAll(exception);
            exceClass = toClass(exceptions);
        }

        String content = "{\n" + body.getContent() + "\n}";
        try {
            CtMethod method = CtNewMethod.make(modifier, returnClass, name, paramClass, exceClass, content, targetClass);
            addAnnotation(method, annotations);
            targetClass.addMethod(method);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    private CtMethod superMethod(CtMethod originMethod, CtClass targetClass) throws NotFoundException, CannotCompileException {
        int modifiers = originMethod.getModifiers();
        String methodName = originMethod.getName();
        CtClass[] parameterTypes = originMethod.getParameterTypes();
        CtClass returnType = originMethod.getReturnType();
        CtClass[] exceptionTypes = originMethod.getExceptionTypes();

        StringBuilder body = new StringBuilder();
        body.append("{ \n");
        if (!returnType.getName().equals("void")) {
            body.append("return ");
        }

        body.append("super.").append(methodName).append("($$);\n}");

        // method copy
        String content = body.toString();
        CtMethod newMethod = CtNewMethod.make(modifiers, returnType, methodName, parameterTypes, exceptionTypes, content, targetClass);
        for (AttributeInfo attribute : originMethod.getMethodInfo().getAttributes()) {
            newMethod.getMethodInfo().addAttribute(attribute);
        }
        newMethod.setBody(content);
        return newMethod;
    }

    @Override
    public Proxy<T> addAnnotationToClass(Annotation... annotations) {
        addAnnotation(targetClass, annotations);
        return this;
    }

    @Override
    public Proxy<T> addAnnotationToMethod(String name, List<Class<?>> paramType, Annotation... annotations) {
        CtMethod method = getMethod(name, paramType);
        addAnnotation(method, annotations);
        return this;
    }

    private CtMethod getMethod(String name, List<Class<?>> paramType) {
        NameUtil.check(name);
        CtClass[] paramClass = toParam(paramType);
        return getMethod(name, paramClass);
    }

    private CtMethod getMethod(String name, CtClass[] paramClass) {
        CtMethod method;
        try {
            method = targetClass.getDeclaredMethod(name, paramClass);
        } catch (NotFoundException e) {
            try {
                method = originClass.getDeclaredMethod(name, paramClass);
                if (java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    throw new ProxyException("Can not add annotation to static method [" + name + "] of super class " + clazz.getName());
                }
                method = superMethod(method, targetClass);
                targetClass.addMethod(method);
            } catch (NotFoundException | CannotCompileException e2) {
                throw new ProxyException("Can not found method [" + name + "] in " + clazz.getName(), e2);
            }
        }
        return method;
    }

    private CtClass[] toParam(List<Class<?>> paramType) {
        if (null == paramType) {
            return EMPTY_PARAM;
        }
        int size = paramType.size();
        if (size == 0) {
            return EMPTY_PARAM;
        }

        CtClass[] parameterTypes = new CtClass[size];
        for (int i = 0; i < size; i++) {
            Class<?> param = paramType.get(i);
            parameterTypes[i] = toClass(param);
        }
        return parameterTypes;
    }

    @Override
    public Proxy<T> addAnnotationToField(String name, Annotation... annotations) {
        CtField field;
        try {
            field = targetClass.getDeclaredField(name);
        } catch (NotFoundException e) {
            return this;
        }
        addAnnotation(field, annotations);
        return this;
    }

    private void addAnnotation(CtClass ctClass, Annotation[] annotations) {
        if (null != annotations && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                ctClass.getClassFile().addAttribute(makeAnnotationsAttribute(targetClass, annotation));
            }
        }
    }

    private void addAnnotation(CtMethod ctMethod, Annotation[] annotations) {
        if (null != annotations && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                ctMethod.getMethodInfo().addAttribute(makeAnnotationsAttribute(ctMethod.getDeclaringClass(), annotation));
            }
        }
    }

    private void addAnnotation(CtField ctField, Annotation[] annotations) {
        if (null != annotations && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                ctField.getFieldInfo().addAttribute(makeAnnotationsAttribute(ctField.getDeclaringClass(), annotation));
            }
        }
    }

    private AnnotationsAttribute makeAnnotationsAttribute(CtClass targetClass, Annotation annotation) {
        Class<? extends java.lang.annotation.Annotation> type = annotation.getType();

        ConstPool cpool = targetClass.getClassFile().getConstPool();

        javassist.bytecode.annotation.Annotation annot = new javassist.bytecode.annotation.Annotation(annotation.getType().getName(), cpool);

        Map<String, Class<?>> memberType = AnnotationUtil.getMemberType(type);

        Map<String, Object> properties = annotation.getProperties();
        if (null != properties) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                String name = entry.getKey();
                Class<?> valueType = memberType.get(name);
                if (null == valueType) {
                    continue;
                }

                MemberValue memberValue = AnnotationUtil.getMemberValue(valueType, entry.getValue(), cpool);
                if (null == memberValue) {
                    continue;
                }

                annot.addMemberValue(name, memberValue);
            }
        }

        AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
        attr.addAnnotation(annot);
        return attr;
    }

    @Override
    public Proxy<T> beforeMethod(String name, List<Class<?>> paramType, BodyStatement statements) {
        if (null == statements) {
            return this;
        }
        String content = statements.getContent();
        if (content.isEmpty()) {
            return this;
        }

        CtMethod method = getMethod(name, paramType);
        try {
            method.insertBefore(content);
        } catch (CannotCompileException e) {
            throw new ProxyException(e);
        }
        return this;
    }

    @Override
    public Proxy<T> afterMethod(String name, List<Class<?>> paramType, BodyStatement statements) {
        if (null == statements) {
            return this;
        }
        String content = statements.getContent();
        if (content.isEmpty()) {
            return this;
        }

        CtMethod method = getMethod(name, paramType);
        try {
            method.insertAfter(content, true);
        } catch (CannotCompileException e) {
            throw new ProxyException(e);
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> create() {
//        ClassUtil.writeFile(targetClass);
        try {
            return (Class<T>) targetClass.toClass();
        } catch (CannotCompileException e) {
            throw new ProxyException("create proxy error from " + clazz.getName(), e);
        }
    }
}
