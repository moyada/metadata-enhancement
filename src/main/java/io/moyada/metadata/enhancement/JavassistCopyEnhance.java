package io.moyada.metadata.enhancement;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * javassist 镜像代理
 * @author xueyikang
 * @since 1.0
 **/
class JavassistCopyEnhance<T> extends JavassistEnhance<T> {

    JavassistCopyEnhance(Class<T> targetClass) {
        super(targetClass, true);
    }

    @Override
    CtClass buildTarget(Class<T> target) {
        ClassPool classPool = ClassPool.getDefault();
        return classPool.makeClass(originClass.getClassFile());
    }
}
