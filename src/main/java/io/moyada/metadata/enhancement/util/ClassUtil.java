package io.moyada.metadata.enhancement.util;

import javassist.CannotCompileException;
import javassist.CtClass;

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
