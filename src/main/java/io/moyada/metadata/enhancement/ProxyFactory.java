package io.moyada.metadata.enhancement;

import io.moyada.metadata.enhancement.proxy.JavassistCopyProxy;
import io.moyada.metadata.enhancement.proxy.JavassistExtendProxy;

/**
 * @author xueyikang
 * @since 1.0
 **/
public final class ProxyFactory {

    private ProxyFactory() {
    }

    public static <T> Proxy<T> extend(Class<T> target) {
        return new JavassistExtendProxy<>(target);
    }

    public static <T> Proxy<T> copy(Class<T> target) {
        return new JavassistCopyProxy<>(target);
    }
}
