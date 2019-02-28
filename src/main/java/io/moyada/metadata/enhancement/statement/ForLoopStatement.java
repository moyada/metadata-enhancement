package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;

/**
 * 循环语句
 * @author xueyikang
 * @since 1.0
 **/
public class ForLoopStatement implements Statement {

    // 初始值
    private VariableStatement init;

    // 条件
    private ConditionStatement condition;

    // 递增步骤
    private AssginStatement step;

    // 循环内容
    private BodyStatement body;

    public ForLoopStatement(VariableStatement init, ConditionStatement condition, AssginStatement step, BodyStatement body) {
        Assert.checkNotNull(condition, "condition");
        this.init = init;
        this.condition = condition;
        this.step = step;
        this.body = null == body ? BodyStatement.EMPTY : body;
    }

    @Override
    public String getContent() {
        String body = this.body.getContent();
        StringBuilder content = new StringBuilder(64 + body.length());
        content.append("for (");
        if (null != init) {
            content.append(init.getContent()).append(" ");
        } else {
            content.append("; ");
        }
        content.append(condition.getContent()).append("; ");
        if (null != step) {
            content.append(step.getApply()).deleteCharAt(content.length() - 1);
        }
        content.append(") {\n");
        content.append(body).append("\n}\n");
        return content.toString();
    }
}
