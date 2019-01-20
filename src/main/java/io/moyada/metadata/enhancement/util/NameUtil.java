package io.moyada.metadata.enhancement.util;

import io.moyada.metadata.enhancement.support.Assert;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class NameUtil {

    public static String getProxyName(String className) {
        int index = className.lastIndexOf('.');
        String simpleName = -1 == index ? className : className.substring(index + 1);
        if (!simpleName.contains("$")) {
            return className + "$_Proxy";
        }
        return className + "_Proxy";
    }

    public static void check(String name) {
        Assert.checkNotNull(name, "name");

        int length = name.length();
        if (0 == length) {
            throw new NullPointerException("name is empty");
        }

        char ch = name.charAt(0);
        if (!isLetter(ch) && !isRule(ch)) {
            throw new IllegalArgumentException("name is illegal.");
        }

        for (int i = 1; i < length; i++) {
            ch = name.charAt(i);
            if (isDigital(ch) || isLetter(ch) || isRule(ch)) {
                continue;
            }
            throw new IllegalArgumentException("name is illegal.");
        }
    }

    /**
     * 是否是数字
     * @param ch 字符
     * @return 是则返回 true
     */
    private static boolean isDigital(char ch) {
        return '0' <= ch && ch <= '9';
    }

    /**
     * 是否是字母
     * @param ch 字符
     * @return 是则返回 true
     */
    private static boolean isLetter(char ch) {
        return ('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z');
    }

    /**
     * 是否合法字符
     * @param ch 字符
     * @return 是则返回 true
     */
    private static boolean isRule(char ch) {
        return '_' == ch || ch == '$';
    }
}
