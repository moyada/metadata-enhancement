package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Value;
import io.moyada.metadata.enhancement.support.Assert;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ForLoopStatement implements Statement {

    private VariableStatement init;
    private ConditionStatement condition;
    private AssginStatement step;
    private BodyStatement body;

    public ForLoopStatement(VariableStatement init, ConditionStatement condition, AssginStatement step, BodyStatement body) {
        Assert.checkNotNull(condition, "condition");
        this.init = init;
        this.condition = condition;
        this.step = step;
        this.body = null == body ? EmptyStatement.INSTANCE : body;
    }

    @Override
    public String getContent() {
        StringBuilder content = new StringBuilder("for (");
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
        content.append(body.getContent()).append("\n}");
        return content.toString();
    }

    public static void main(String[] args) {
        ForLoopStatement statement = new ForLoopStatement(
                new VariableStatement(int.class, "id", Value.of(1)),
                new ConditionStatement(ConditionType.LT, IdentStatement.of("id"), IdentStatement.of("50")),
                new OperatorStatement(IdentStatement.of("id"), Operator.PLUS, Value.of(12)).assign(IdentStatement.of("id")),
                BodyStatement.init()
                        .addStatement(new VariableStatement(String.class, "name", Value.of("success")))
                        .addStatement(InvokeStatement.of(IdentStatement.of("name"), "toString"))
                        .addStatement(InvokeStatement.of(IdentStatement.of("System.out"), "println", Value.of(IdentStatement.of("name"))))
        );

        System.out.println(statement.getContent());
    }
}
