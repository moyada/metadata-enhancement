package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;
import io.moyada.metadata.enhancement.support.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class OperatorStatement extends AssginStatement implements Assign, Statement {

    private Value self;

    private List<String> opts;

    public OperatorStatement(Value self, Operator operator, Value value) {
        Assert.checkNotNull(self, "self");
        this.self = self;
        this.opts = new ArrayList<>();
        operate(operator, value);
    }

    public OperatorStatement operate(Operator operator, Value value) {
        Assert.checkNotNull(operator, "operator");
        Assert.checkNotNull(value, "value");
        opts.add(operator.getOpt() + value.getStatement());
        return this;
    }

    @Override
    protected String getInvoke() {
        StringBuilder content = new StringBuilder();
        content.append(self.getStatement());
        for (String opt : opts) {
            content.append(opt);
        }
        return content.toString();
    }
}
