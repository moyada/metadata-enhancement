package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Value;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class VariableStatement extends IdentStatement implements Statement {

    Class<?> type;

    private Value init;

    public VariableStatement(Class<?> type, String name) {
        this(type, name, null);
    }

    public VariableStatement(Class<?> type, String name, Value init) {
        super(name);
        this.type = type;
        this.init = init;
    }

    @Override
    public String getContent() {
        StringBuilder content = new StringBuilder(type.getSimpleName()).append(" ").append(identify);
        if (null != init) {
            content.append(" = ").append(init.getStatement());
        }
        content.append(";");
        return content.toString();
    }

    public static void main(String[] args) {
        System.out.println(new VariableStatement(String.class, "test").getContent());
        System.out.println(new VariableStatement(String.class, "test", Value.of("name")).getContent());
    }
}
