package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断语句
 * @author xueyikang
 * @since 1.0
 **/
public class IfStatement implements Statement {

    // 条件
    private ConditionStatement ifState;

    // 通过内容
    private BodyStatement ifBody;

    // 非法内容
    private BodyStatement elseBody;

    // 中间条件
    private List<IfStatement> elseIf;

    private IfStatement(ConditionStatement ifState, BodyStatement ifBody) {
        this.ifState = ifState;
        this.ifBody = null == ifBody ? BodyStatement.EMPTY : ifBody;
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
        this.elseBody = null == body ? BodyStatement.EMPTY : body;
        return this;
    }

    @Override
    public String getContent() {
        String body = ifBody.getContent();
        StringBuilder content = new StringBuilder(null == elseBody ? 32 + body.length() : 128 + body.length());
        content.append("if (").append(ifState.getContent()).append(") ");
        content.append("{\n").append(body).append("\n}");
        if (null != elseIf) {
            for (IfStatement statement : elseIf) {
                content.append(" else ").append(statement.getContent()).deleteCharAt(content.length() - 1);
            }
        }
        if (null != elseBody) {
            content.append(" else {\n").append(elseBody.getContent()).append("\n}");
        }
        content.append("\n");
        return content.toString();
    }
}
