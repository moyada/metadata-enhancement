package io.moyada.metadata.enhancement.statement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Assign {

    AssginStatement assign(IdentStatement ident);

    AssginStatement assign(VariableStatement ident);

    String getApply();

    IdentStatement toIdent();
}
