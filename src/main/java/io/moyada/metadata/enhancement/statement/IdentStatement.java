package io.moyada.metadata.enhancement.statement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class IdentStatement implements Statement {

    public static final IdentStatement NULL = new IdentStatement("null");

    private final String identify;

    IdentStatement(String name) {
        identify = name;
    }

    public static IdentStatement of(String name) {
        return new IdentStatement(name);
    }

    public static IdentStatement of(int index) {
        if (index < 1) {
            throw new IllegalArgumentException("IdentStatement index must be positive.");
        }
        return new IdentStatement("$" + index);
    }

    @Override
    public String getContent() {
        return identify;
    }

    public String getIdentify() {
        return identify;
    }
}
