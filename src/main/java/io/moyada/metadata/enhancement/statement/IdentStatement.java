package io.moyada.metadata.enhancement.statement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class IdentStatement implements Statement {

    private final String identify;

    public IdentStatement(String name) {
        identify = name;
    }

    public static IdentStatement of(String name) {
        return new IdentStatement(name);
    }

    public static IdentStatement of(int index) {
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
