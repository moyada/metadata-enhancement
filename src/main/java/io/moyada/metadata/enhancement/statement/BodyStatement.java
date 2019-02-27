package io.moyada.metadata.enhancement.statement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class BodyStatement implements Statement {

    public static final BodyStatement EMPTY = new BodyStatement() {
        @Override
        public BodyStatement addStatement(Statement statement) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getContent() {
            return "";
        }
    };

    private List<Statement> statementList;

    BodyStatement() {
    }

    public static BodyStatement init() {
        return new BodyStatement();
    }

    public BodyStatement addStatement(Statement statement) {
        if (null == statementList) {
            this.statementList = new ArrayList<>();
        }
        statementList.add(statement);
        return this;
    }

    @Override
    public String getContent() {
        if (null == statementList) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (Statement statement : statementList) {
            str.append(statement.getContent()).append("\n");
        }
        return str.deleteCharAt(str.length() - 1).toString();
    }
}
