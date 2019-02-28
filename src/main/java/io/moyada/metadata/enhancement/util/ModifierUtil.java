package io.moyada.metadata.enhancement.util;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ModifierUtil {

    /**
     * 获取展示标识
     * @param flag 权限
     * @return
     */
    public static String valueOf(int flag) {
        if (flag < 0) {
            throw new IllegalArgumentException("flag must be positive, promote use Modifier.PUBLIC");
        }

        String str;
        if (java.lang.reflect.Modifier.isPrivate(flag)) {
            str = "private ";
        } else if (java.lang.reflect.Modifier.isProtected(flag)) {
            str = "protected ";
        } else if (java.lang.reflect.Modifier.isPublic(flag)) {
            str = "public ";
        } else {
            str = " ";
        }

        if (java.lang.reflect.Modifier.isStatic(flag)) {
            str += "static ";
        }

        if (java.lang.reflect.Modifier.isFinal(flag)) {
            str += "final ";
        }

        return str;
    }
}
