package io.moyada.metadata.enhancement;

import io.moyada.metadata.enhancement.util.NameUtil;
import javassist.*;
import javassist.bytecode.AttributeInfo;

/**
 * javassist 继承代理
 * @author xueyikang
 * @since 0.0.1
 **/
class JavassistExtendEnhance<T> extends JavassistEnhance<T> {

    JavassistExtendEnhance(Class<T> target) {
        super(target);
    }

    @Override
    CtClass buildTarget(Class<T> target) throws CannotCompileException {
        ClassPool classPool = ClassPool.getDefault();

        // 继承方式
        CtClass ctClass = classPool.makeClass(NameUtil.getProxyName(target.getName()));
        ctClass.setSuperclass(originClass);

        // 注解
        for (AttributeInfo attribute : originClass.getClassFile().getAttributes()) {
            ctClass.getClassFile().addAttribute(attribute);
        }

        // 构造方法
        CtConstructor ctConstructor;
        for (CtConstructor constructor : originClass.getConstructors()) {
            ctConstructor = CtNewConstructor.copy(constructor, ctClass, null);
            for (AttributeInfo attribute : constructor.getMethodInfo().getAttributes()) {
                ctConstructor.getMethodInfo().addAttribute(attribute);
            }
            ctConstructor.setBody("super($$);");

            ctClass.addConstructor(ctConstructor);
        }
        return ctClass;
    }
}
