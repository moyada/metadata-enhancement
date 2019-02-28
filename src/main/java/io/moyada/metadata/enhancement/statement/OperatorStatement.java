package io.moyada.metadata.enhancement.statement;

import io.moyada.metadata.enhancement.support.Assert;
import io.moyada.metadata.enhancement.support.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * 算术操作语句
 * @author xueyikang
 * @since 1.0
 **/
public class OperatorStatement extends AssginStatement implements Assign, Statement {

    // 目标
    private Value self;

    // 操作链
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
        StringBuilder content = new StringBuilder(6 + opts.size() * 6);
        content.append(self.getStatement());
        for (String opt : opts) {
            content.append(opt);
        }
        return content.toString();
    }
}
