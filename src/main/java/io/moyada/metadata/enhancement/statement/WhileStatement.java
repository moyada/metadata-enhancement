package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;

/**
 * 循环语句
 * @author xueyikang
 * @since 1.0
 **/
public class WhileStatement implements Statement {

    // 条件
    private ConditionStatement condition;

    // 内容
    private BodyStatement body;

    public WhileStatement(ConditionStatement condition, BodyStatement body) {
        Assert.checkNotNull(condition, "condition");
        this.condition = condition;
        this.body = null == body ? BodyStatement.EMPTY : body;
    }

    @Override
    public String getContent() {
        String content = "while (" + condition.getContent() + ") " +
                "{\n" + body.getContent() + "\n}\n";
        return content;
    }
}
