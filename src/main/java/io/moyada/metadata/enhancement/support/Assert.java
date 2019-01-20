package io.moyada.metadata.enhancement.support;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Assert {

    public static void checkNotNull(Object obj, String name) {
        if (null == obj) {
            throw new NullPointerException(name + " is null");
        }
    }
}
