package io.moyada.metadata.enhancement;

import io.moyada.metadata.enhancement.exception.EnhanceException;
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
public abstract class JavassistEnhance<T> implements Enhance<T> {

    private static final CtClass[] EMPTY_PARAM = new CtClass[0];

    CtClass originClass;
    CtClass targetClass;

    private final Set<String> importPackage;

    JavassistEnhance(Class<T> targetClass) {
        try {
            this.originClass = getOrigin(targetClass);
            this.targetClass = buildTarget(targetClass);
        } catch (NotFoundException e) {
            throw new EnhanceException(targetClass.getName() + " not found.", e);
        } catch (CannotCompileException e) {
            throw new EnhanceException("Can not fetch target class: " + originClass.getName(), e);
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

    private CtClass getOrigin(Class<T> target) throws NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        return classPool.getCtClass(target.getName());
    }

    abstract CtClass buildTarget(Class<T> target) throws NotFoundException, CannotCompileException;

    @Override
    public Enhance<T> addImport(String name) {
        Assert.checkNotNull(name, "class name");
        addPackage(name);
        return this;
    }

    @Override
    public Enhance<T> addField(String name, Class<?> type, int modifier) {
        return addField(name, type, modifier, new Annotation[0]);
    }

    @Override
    public Enhance<T> addField(String name, Class<?> type, int modifier, Value init) {
        return addField(name, type, modifier, init, new Annotation[0]);
    }

    @Override
    public Enhance<T> addField(String name, Class<?> type, int modifier, Annotation... annotations) {
        return addField(name, type, modifier, null, annotations);
    }

    @Override
    public Enhance<T> addField(String name, Class<?> type, int modifier, Value init, Annotation... annotations) {
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

    private CtClass[] toClass(Class<?>[] classes) {
        if (null == classes) {
            return new CtClass[0];
        }
        int size = classes.length;
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
            throw new EnhanceException(e);
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
            throw new EnhanceException(e);
        }

        ctField.setModifiers(modifier);
        ctField.setType(ctClass);
        ctField.setName(name);

        return ctField;
    }

    @Override
    public Enhance<T> addMethod(String name, Class<?>[] paramType, Class<?> returnType, Class<? extends Throwable>[] exception,
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
            exceClass = toClass(exception);
        }

        String content = "{" + body.getContent() + "}";

        try {
            CtMethod method = CtNewMethod.make(modifier, returnClass, name, paramClass, exceClass, content, targetClass);
            addAnnotation(method, annotations);
            targetClass.addMethod(method);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return this;
    }

    private CtMethod createSuperMethod(CtMethod originMethod, CtClass targetClass) throws NotFoundException, CannotCompileException {
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
    public Enhance<T> addAnnotationToClass(Annotation... annotations) {
        addAnnotation(targetClass, annotations);
        return this;
    }

    @Override
    public Enhance<T> addAnnotationToMethod(String name, Class<?>[] paramType, Annotation... annotations) {
        CtMethod method = getMethod(name, paramType);
        addAnnotation(method, annotations);
        return this;
    }

    private CtMethod getMethod(String name, Class<?>[] paramType) {
        NameUtil.check(name);
        CtClass[] paramClass = toParam(paramType);
        return getMethod(name, paramClass);
    }

    private CtMethod getMethod(String name, CtClass[] paramClass) {
        CtMethod method = null;
        try {
            method = targetClass.getDeclaredMethod(name, paramClass);
        } catch (NotFoundException e) {
            // pass
        }

        if (method != null) {
            ClassUtil.checkMethod(targetClass, method);
            return method;
        }

        CtClass currentClass;

        do {
            try {
                currentClass = targetClass.getSuperclass();
            } catch (NotFoundException e) {
                throw new EnhanceException("Can not found method [" + name + "] in " + targetClass.getName(), e);
            }

            if ("java.lang.Object".equals(currentClass.getName())) {
                throw new EnhanceException("Can not found method [" + name + "] in " + targetClass.getName());
            }

            try {
                method = currentClass.getDeclaredMethod(name, paramClass);
            } catch (NotFoundException e) {
                throw new EnhanceException("Can not found method [" + name + "] in " + targetClass.getName(), e);
            }
        } while (method == null);

        ClassUtil.checkMethod(currentClass, method);

        try {
            method = createSuperMethod(method, this.targetClass);
            this.targetClass.addMethod(method);
        } catch (CannotCompileException | NotFoundException e) {
            throw new EnhanceException("Enhance method [" + name + "] error.", e);
        }
        return method;
    }

    private CtClass[] toParam(Class<?>[] paramType) {
        if (null == paramType) {
            return EMPTY_PARAM;
        }
        int size = paramType.length;
        if (size == 0) {
            return EMPTY_PARAM;
        }

        CtClass[] parameterTypes = new CtClass[size];
        for (int i = 0; i < size; i++) {
            Class<?> param = paramType[i];
            parameterTypes[i] = toClass(param);
        }
        return parameterTypes;
    }

    @Override
    public Enhance<T> addAnnotationToField(String name, Annotation... annotations) {
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
    public Enhance<T> beforeMethod(String name, Class<?>[] paramType, BodyStatement statements) {
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
            throw new EnhanceException(e);
        }
        return this;
    }

    @Override
    public Enhance<T> afterMethod(String name, Class<?>[] paramType, BodyStatement statements) {
        return afterMethod(name, paramType, statements, false);
    }

    @Override
    public Enhance<T> afterMethod(String name, Class<?>[] paramType, BodyStatement statements, boolean asFinally) {
        if (null == statements) {
            return this;
        }
        String content = statements.getContent();
        if (content.isEmpty()) {
            return this;
        }

        CtMethod method = getMethod(name, paramType);
        try {
            method.insertAfter(content, asFinally);
        } catch (CannotCompileException e) {
            throw new EnhanceException(e);
        }
        return this;
    }

    @Override
    public Class<T> create() {
        return create(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> create(String name) {
        if (null == name) {
            targetClass.setName(NameUtil.getProxyName(originClass.getName()));
        } else {
            targetClass.setName(name);
        }

        if (originClass.getName().equals(targetClass.getName())) {
            throw new EnhanceException("duplicate definition class name");
        }

        Class<T> newClass;
        try {
            newClass = (Class<T>) targetClass.toClass();
        } catch (CannotCompileException e) {
            throw new EnhanceException("create proxy error from " + originClass.getName(), e);
        }

        if (ClassUtil.isPrint()) {
            ClassUtil.writeFile(targetClass);
        }
        return newClass;
    }
}
