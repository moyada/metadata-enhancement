package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;
import io.moyada.metadata.enhancement.support.Value;

/**
 * 返回语句
 * @author xueyikang
 * @since 1.0
 **/
public class ReturnStatement implements Statement {

    // 返回值
    private Value value;

    public ReturnStatement(Value value) {
        Assert.checkNotNull(value, "value");
        this.value = value;
    }

    @Override
    public String getContent() {
        return "return " + value.getStatement() + ";";
    }
}
