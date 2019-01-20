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
