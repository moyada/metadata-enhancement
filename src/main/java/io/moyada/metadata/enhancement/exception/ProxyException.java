package io.moyada.metadata.enhancement.exception;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ProxyException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public ProxyException(String message) {
        super(message);
    }

    public ProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyException(Throwable cause) {
        super(cause);
    }
}
