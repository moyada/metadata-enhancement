package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class IfStatement implements Statement {

    private ConditionStatement ifState;
    private BodyStatement ifBody;
    private BodyStatement elseBody;

    private List<IfStatement> elseIf;

    private IfStatement(ConditionStatement ifState, BodyStatement ifBody) {
        this.ifState = ifState;
        this.ifBody = null == ifBody ? EmptyStatement.INSTANCE : ifBody;
    }

    public static IfStatement If(ConditionStatement condition, BodyStatement body) {
        Assert.checkNotNull(condition, "condition statement");
        return new IfStatement(condition, body);
    }

    public IfStatement ElseIf(ConditionStatement condition, BodyStatement body) {
        Assert.checkNotNull(condition, "condition statement");
        if (null == elseIf) {
            elseIf = new ArrayList<>();
        }
        elseIf.add(new IfStatement(condition, body));
        return this;
    }

    public Statement Else(BodyStatement body) {
        this.elseBody = null == body ? EmptyStatement.INSTANCE : body;
        return this;
    }

    @Override
    public String getContent() {
        StringBuilder content = new StringBuilder("if (");
        content.append(ifState.getContent()).append(") ");
        content.append("{\n").append(ifBody.getContent()).append("\n}");
        if (null != elseIf) {
            for (IfStatement statement : elseIf) {
                content.append(" else ").append(statement.getContent());
            }
        }
        if (null != elseBody) {
            content.append(" else {\n").append(elseBody.getContent()).append("}");
        }
        content.append("\n");
        return content.toString();
    }

    public static void main(String[] args) {
        Statement statement = IfStatement.If(new ConditionStatement(ConditionType.LE, IdentStatement.of(1), IdentStatement.of("12")), null)
                .ElseIf(new ConditionStatement(ConditionType.GT, IdentStatement.of(1), IdentStatement.of("size")), null)
                .ElseIf(new ConditionStatement(ConditionType.EQ, IdentStatement.of(1), IdentStatement.of("size")), null)
                .Else(null);

        System.out.println(statement.getContent());
    }
}