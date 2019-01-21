package io.moyada.metadata.enhancement.statement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public enum Operator {

    PLUS(" + "),

    MINUS(" - "),

    TIMES(" * "),

    DIVIDE(" / "),

    EQUALS(" = "),
    ;

    private String opt;

    Operator(String opt) {
        this.opt = opt;
    }

    public String getOpt() {
        return opt;
    }
}
