package io.moyada.metadata.enhancement.statement;


import io.moyada.metadata.enhancement.support.Value;
import io.moyada.metadata.enhancement.util.NameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class InvokeStatement extends AssginStatement implements Statement {

    private IdentStatement self;

    private List<String> invokes;

    private InvokeStatement(IdentStatement self) {
        this.self = self;
        this.invokes = new ArrayList<>();
    }

    public static InvokeStatement of(IdentStatement self, String method, Value... param) {
        InvokeStatement statement = new InvokeStatement(self);
        return statement.of(method, param);
    }

    public InvokeStatement of(String method, Value... param) {
        NameUtil.check(method);
        invokes.add(buildApply(method, param));
        return this;
    }

    public IdentStatement toIdent() {
        StringBuilder content = new StringBuilder();
        content.append(self.getContent());
        for (String invoke : invokes) {
            content.append('.').append(invoke);
        }
        return IdentStatement.of(content.toString());
    }

    private String buildApply(String method, Value[] params) {
        if (null == params) {
            return method + "()";
        }
        int length = params.length;
        if (length == 0) {
            return method + "()";
        }

        StringBuilder invoke = new StringBuilder(method);
        invoke.append("(");
        for (Value param : params) {
            invoke.append(param.getStatement());
        }
        invoke.append(")");
        return invoke.toString();
    }

    @Override
    protected String getInvoke() {
        StringBuilder content = new StringBuilder();
        content.append(self.getContent());
        for (String invoke : invokes) {
            content.append('.').append(invoke);
        }
        content.append(";");
        return content.toString();
    }

    public static void main(String[] args) {
        Statement statement = of(IdentStatement.of(1), "apply", Value.of(IdentStatement.of("name")))
                .of("toString", Value.of("haha"));

        System.out.println(statement.getContent());
    }
}
