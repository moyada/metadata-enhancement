package io.moyada.metadata.enhancement.statement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AssginStatement implements Assign, Statement {

    private Class<?> type;

    private String name;

    @Override
    public AssginStatement assign(IdentStatement ident) {
        this.name = ident.getIdentify();
        return this;
    }

    @Override
    public AssginStatement assign(VariableStatement ident) {
        this.type = ident.type;
        return assign((IdentStatement) ident);
    }

    @Override
    public String getApply() {
        String apply;
        if (null == name) {
            apply = getInvoke();
        } else {
            apply = name + " = " + getInvoke();
        }
        return apply + ";";
    }

    @Override
    public IdentStatement toIdent() {
        return IdentStatement.of(getInvoke());
    }

    protected abstract String getInvoke();

    @Override
    public String getContent() {
        String content;
        if (null == type) {
            content = getApply();
        } else {
            content = type.getSimpleName() + " " + getApply();
        }
        return content;
    }
}
