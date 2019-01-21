package io.moyada.metadata.enhancement.support;

import io.moyada.metadata.enhancement.statement.IdentStatement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Value {

    private String statement;

    private Class<?> type;

    public Value(String statement) {
        this.statement = statement;
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

    public static Value of(IdentStatement ident) {
        return new Value(ident.getIdentify());
    }

    public static Value of(Class<?> type) {
        Value statement = new Value(type.getSimpleName() + ".class");
        statement.type = type;
        return statement;
    }

    public static Value newObject(Class<?> type, Object... params) {
        StringBuilder builder = new StringBuilder("new ").append(type.getSimpleName());
        buildParam(builder, params);

        Value statement = new Value(builder.toString());
        statement.type = type;
        return statement;
    }

    public static Value staticMethod(Class<?> type, String methodName, Object... params) {
        StringBuilder builder = new StringBuilder(type.getSimpleName()).append(".").append(methodName);
        buildParam(builder, params);

        Value statement = new Value(builder.toString());
        statement.type = type;
        return statement;
    }

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

    public static Value ref(Class<?> type, String fieldName) {
        Value statement = new Value(type.getSimpleName() + "." + fieldName);
        statement.type = type;
        return statement;
    }

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
