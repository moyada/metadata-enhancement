package io.moyada.metadata.enhancement.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class TypeUtil {

    private final static Map<Class<?>, Class<?>> primitiveMap = new HashMap<>();
    static {
        primitiveMap.put(boolean.class, Boolean.class);
        primitiveMap.put(byte.class, Byte.class);
        primitiveMap.put(short.class, Short.class);
        primitiveMap.put(char.class, Character.class);
        primitiveMap.put(int.class, Integer.class);
        primitiveMap.put(long.class, Long.class);
        primitiveMap.put(float.class, Float.class);
        primitiveMap.put(double.class, Double.class);
    }

    public static boolean isBaseType(String className) {
        if (className.startsWith("java.lang")) {
            return className.lastIndexOf('.') < 10;
        }
        switch (className) {
            case "boolean":
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "char":
            case "void":
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getType(Object value, Class<T> type) {
        if (type.isPrimitive()) {
            type = (Class<T>) primitiveMap.get(type);
        }

        if (!type.isInstance(value)) {
            throw new ClassCastException(value + " can not convert to " + type);
        }
        return (T) value;
    }
}
