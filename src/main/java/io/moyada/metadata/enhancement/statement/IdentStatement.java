package io.moyada.metadata.enhancement.statement;

/**
 * 值引用语句
 * @author xueyikang
 * @since 1.0
 **/
public class IdentStatement implements Statement {

    public static final IdentStatement NULL = new IdentStatement("null");

    // 语句
    private final String identify;

    IdentStatement(String name) {
        identify = name;
    }

    /**
     * 值引用
     * @param name 值名
     * @return
     */
    public static IdentStatement of(String name) {
        return new IdentStatement(name);
    }

    /**
     * 获取方法参数
     * @param index 参数下标，从 1 开始
     * @return
     */
    public static IdentStatement of(int index) {
        if (index < 1) {
            throw new IllegalArgumentException("IdentStatement index must be positive.");
        }
        return new IdentStatement("$" + index);
    }

    @Override
    public String getContent() {
        return identify;
    }

    public String getIdentify() {
        return identify;
    }
}
