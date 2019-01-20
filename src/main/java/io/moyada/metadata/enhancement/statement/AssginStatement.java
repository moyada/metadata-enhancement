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
        this.name = ident.identify;
        return this;
    }

    @Override
    public AssginStatement assign(VariableStatement ident) {
        this.type = ident.type;
        return assign((IdentStatement) ident);
    }

    @Override
    public String getApply() {
        if (null == name) {
            return getInvoke();
        }
        return name + " = " + getInvoke();
    }

    protected abstract String getInvoke();

    @Override
    public String getContent() {
        if (null == type) {
            return getApply();
        }
        return type.getSimpleName() + " " + getApply();
    }
}
