package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ConditionStatement implements Statement {

    private ConditionType type;
    private IdentStatement left;
    private IdentStatement right;

    public ConditionStatement(ConditionType type, IdentStatement left, IdentStatement right) {
        Assert.checkNotNull(type, "condition type");
        Assert.checkNotNull(left, "left identify");
        Assert.checkNotNull(right, "right identify");

        this.type = type;
        this.left = left;
        this.right = right;
    }

    @Override
    public String getContent() {
        return left.getContent() + type.getTag() + right.getContent();
    }
}
