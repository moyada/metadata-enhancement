package io.moyada.metadata.enhancement.statement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public enum ConditionType {

    LT(" < "),
    LE(" <= "),
    GT(" > "),
    GE(" >= "),
    EQ(" == "),
    NE(" != "),
    ;

    private String tag;

    ConditionType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
