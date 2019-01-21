package io.moyada.metadata.enhancement;

/**
 * @author xueyikang
 * @since 1.0
 **/
public final class EnhanceFactory {

    private EnhanceFactory() {
    }

    public static <T> Enhance<T> extend(Class<T> target) {
        return new JavassistExtendEnhance<>(target);
    }

    public static <T> Enhance<T> copy(Class<T> target) {
        return new JavassistCopyEnhance<>(target);
    }
}
