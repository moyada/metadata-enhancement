package io.moyada.metadata.enhancement.support;

import io.moyada.metadata.enhancement.statement.IdentStatement;

/**
 * 值引用
 * @author xueyikang
 * @since 1.0
 **/
public class Value {

    // 值语句
    private String statement;

    // 引入类型
    private Class<?> type;

    public Value(String statement) {
        this.statement = statement;
    }

    public static Value of(Object value) {
        String name = value.getClass().getName();
        switch (name) {
            case "java.lang.Byte" : return of((byte) value);
            case "java.lang.Short" : return of((short) value);
            case "java.lang.Integer" : return of((int) value);
            case "java.lang.Long" : return of((long) value);
            case "java.lang.Float" : return of((float) value);
            case "java.lang.Double" : return of((double) value);
            case "java.lang.Boolean" : return of((boolean) value);
            case "java.lang.Character" : return of((char) value);
            case "java.lang.String" : return of((String) value);
            default: throw new IllegalArgumentException(name + " not found.");
        }
    }

    public static Value of(byte value) {
        return new Value(Byte.toString(value));
    }

    public static Value of(short value) {
        return new Value(Short.toString(value));
    }

    public static Value of(int value) {
        return new Value(Integer.toString(value));
    }

    public static Value of(long value) {
        return new Value(value + "L");
    }

    public static Value of(float value) {
        return new Value(value + "F");
    }

    public static Value of(double value) {
        return new Value(value + "D");
    }

    public static Value of(boolean value) {
        return new Value(Boolean.toString(value));
    }

    public static Value of(char value) {
        return new Value("\'" + value + "\'");
    }

    public static Value of(String value) {
        return new Value("\"" + value + "\"");
    }

    /**
     * 语句块
     * @param ident
     * @return
     */
    public static Value of(IdentStatement ident) {
        return new Value(ident.getIdentify());
    }

    /**
     * 类型
     * @param type
     * @return
     */
    public static Value of(Class<?> type) {
        Value statement = new Value(type.getSimpleName() + ".class");
        statement.type = type;
        return statement;
    }

    /**
     * 类型构造函数
     * @param type
     * @param params
     * @return
     */
    public static Value newObject(Class<?> type, Object... params) {
        StringBuilder builder = new StringBuilder("new ").append(type.getSimpleName());
        buildParam(builder, params);

        Value statement = new Value(builder.toString());
        statement.type = type;
        return statement;
    }

    /**
     * 静态方法构造
     * @param type
     * @param methodName
     * @param params
     * @return
     */
    public static Value staticMethod(Class<?> type, String methodName, Object... params) {
        StringBuilder builder = new StringBuilder(type.getSimpleName()).append(".").append(methodName);
        buildParam(builder, params);

        Value statement = new Value(builder.toString());
        statement.type = type;
        return statement;
    }

    /**
     * 创建参数列表
     * @param builder
     * @param values
     */
    private static void buildParam(StringBuilder builder, Object[] values) {
        builder.append("(");
        if (values.length != 0) {
            for (Object value : values) {
                append(builder, value);
                builder.append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append(")");
    }

    private static void append(StringBuilder builder, Object value) {
        Class<?> paramClass = value.getClass();

        boolean isStr = paramClass.equals(String.class);
        boolean isChar = paramClass.equals(Character.class);
        if (isStr) {
            builder.append("\"");
        } else if (isChar) {
            builder.append("\'");
        }

        builder.append(value);

        if (isStr) {
            builder.append("\"");
        } else if (isChar) {
            builder.append("\'");
        }
    }

    /**
     * 静态公开属性引用
     * @param type
     * @param fieldName
     * @return
     */
    public static Value ref(Class<?> type, String fieldName) {
        Value statement = new Value(type.getSimpleName() + "." + fieldName);
        statement.type = type;
        return statement;
    }

    /**
     * 枚举引用
     * @param type
     * @return
     */
    public static Value ref(Enum<?> type) {
        return ref(type.getClass(), type.name());
    }

    public String getStatement() {
        return statement;
    }

    public Class<?> getType() {
        return type;
    }
}
