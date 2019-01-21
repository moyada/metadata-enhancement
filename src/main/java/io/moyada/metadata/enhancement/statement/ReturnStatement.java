package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;
import io.moyada.metadata.enhancement.support.Value;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ReturnStatement implements Statement {

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
