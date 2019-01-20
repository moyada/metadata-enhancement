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

    private IdentStatement self;

    private List<String> opts;

    public OperatorStatement(IdentStatement self, Operator operator, Value value) {
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
        content.append(self.getContent());
        for (String opt : opts) {
            content.append(opt);
        }
        content.append(";");
        return content.toString();
    }

    public static void main(String[] args) {
        Statement statement = new OperatorStatement(IdentStatement.of("index"), Operator.PLUS, Value.of(12))
                .operate(Operator.TIMES, Value.of(21))
                .assign(new VariableStatement(int.class, "name"));

        System.out.println(statement.getContent());
    }
}
