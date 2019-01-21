package io.moyada.metadata.enhancement.exception;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class EnhanceException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public EnhanceException(String message) {
        super(message);
    }

    public EnhanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnhanceException(Throwable cause) {
        super(cause);
    }
}
