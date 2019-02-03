package io.moyada.metadata.enhancement.util;

import io.moyada.metadata.enhancement.exception.EnhanceException;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;
import java.net.URL;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ClassUtil {

    private static final boolean isPrint;

    static {
        String property = System.getProperty("print.result");
        if (property == null) {
            isPrint = false;
        } else {
            isPrint = Boolean.TRUE.toString().equalsIgnoreCase(property);
        }
    }

    public static boolean isPrint() {
        return isPrint;
    }

    public static void checkMethod(CtClass ctClass, CtMethod method) {
        int modifiers = method.getModifiers();
        if (java.lang.reflect.Modifier.isStatic(modifiers)) {
            throw new EnhanceException("Can not enhance static method [" + method.getName() + "] of super class " + ctClass.getName());
        }
        if (java.lang.reflect.Modifier.isPrivate(modifiers)) {
            throw new EnhanceException("Can not enhance private method [" + method.getName() + "] of super class " + ctClass.getName());
        }
    }

    public static void writeFile(CtClass ctClass) {
        URL url = ClassLoader.getSystemResource("");
        String path = url.getPath();
        try {
            ctClass.writeFile(path);
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
