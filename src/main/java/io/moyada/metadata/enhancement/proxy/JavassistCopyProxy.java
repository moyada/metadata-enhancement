package io.moyada.metadata.enhancement.proxy;

import io.moyada.metadata.enhancement.util.NameUtil;
import javassist.ClassPool;
import javassist.CtClass;

/**
 * javassist 镜像代理
 * @author xueyikang
 * @since 0.0.1
 **/
public class JavassistCopyProxy<T> extends JavassistProxy<T> {

    public JavassistCopyProxy(Class<T> targetClass) {
        super(targetClass);
    }

    @Override
    CtClass buildTarget(Class<T> target) {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(originClass.getClassFile());
        ctClass.setName(NameUtil.getProxyName(ctClass.getName()));
        return ctClass;
    }
}
