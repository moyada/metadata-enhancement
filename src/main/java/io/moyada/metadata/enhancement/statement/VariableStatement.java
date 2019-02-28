package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Value;

/**
 * 属性值语句
 * @author xueyikang
 * @since 1.0
 **/
public class VariableStatement extends IdentStatement implements Statement {

    // 类型
    protected Class<?> type;

    // 初始值
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
        StringBuilder content = new StringBuilder(type.getSimpleName()).append(" ").append(getIdentify());
        if (null != init) {
            content.append(" = ").append(init.getStatement());
        }
        content.append(";");
        return content.toString();
    }
}
