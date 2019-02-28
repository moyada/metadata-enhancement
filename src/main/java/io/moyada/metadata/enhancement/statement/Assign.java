package io.moyada.metadata.enhancement.statement;

/**
 * 赋值
 * @author xueyikang
 * @since 1.0
 **/
public interface Assign {

    /**
     * 引用语句
     * @param ident
     * @return
     */
    AssginStatement assign(IdentStatement ident);

    /**
     * 引用值
     * @param ident
     * @return
     */
    AssginStatement assign(VariableStatement ident);

    /**
     * 获取调用语句
     * @return
     */
    String getApply();

    /**
     * 返回引用语句
     * @return
     */
    IdentStatement toIdent();
}
