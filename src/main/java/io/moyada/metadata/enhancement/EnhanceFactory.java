package io.moyada.metadata.enhancement;

/**
 * 代理工厂
 * @author xueyikang
 * @since 1.0
 * not thread safe
 **/
public final class EnhanceFactory {

    private EnhanceFactory() {
    }

    /**
     * 继承方式
     * @param target 目标类
     * @param <T> 类型
     * @return
     */
    public static <T> Enhance<T> extend(Class<T> target) {
        return new JavassistExtendEnhance<>(target);
    }

    /**
     * 拷贝方式
     * @param target 目标类
     * @param <T> 类型
     * @return
     */
    public static <T> Enhance<T> copy(Class<T> target) {
        return new JavassistCopyEnhance<>(target);
    }
}
