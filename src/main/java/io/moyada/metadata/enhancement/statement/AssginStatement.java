package io.moyada.metadata.enhancement.statement;

/**
 * 赋值语句
 * @author xueyikang
 * @since 1.0
 **/
public abstract class AssginStatement implements Assign, Statement {

    // 类型
    private Class<?> type;

    // 名称
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

    /**
     * 调用语句
     * @return
     */
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
