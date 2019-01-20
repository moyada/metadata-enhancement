package io.moyada.metadata.enhancement.statement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public final class EmptyStatement extends BodyStatement implements Statement {

    public static final BodyStatement INSTANCE = new EmptyStatement();

    private EmptyStatement() {
    }

    public static BodyStatement init() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BodyStatement addStatement(Statement statement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContent() {
        return "";
    }
}
