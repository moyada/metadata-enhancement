package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class WhileStatement implements Statement {

    private ConditionStatement condition;
    private BodyStatement body;

    public WhileStatement(ConditionStatement condition, BodyStatement body) {
        Assert.checkNotNull(condition, "condition");
        this.condition = condition;
        this.body = null == body ? EmptyStatement.INSTANCE : body;
    }

    @Override
    public String getContent() {
        StringBuilder content = new StringBuilder("while (");
        content.append(condition.getContent()).append(") ");
        content.append("{\n").append(body.getContent()).append("}");
        return content.toString();
    }

    public static void main(String[] args) {
        Statement statement = new WhileStatement(new ConditionStatement(ConditionType.LE, IdentStatement.of(1), IdentStatement.of("size")), null);
        System.out.println(statement.getContent());
    }
}
