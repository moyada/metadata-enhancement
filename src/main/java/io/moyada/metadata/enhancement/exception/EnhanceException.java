package io.moyada.metadata.enhancement.exception;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class EnhanceException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public EnhanceException(String message, Throwable cause) {
        super(message, cause, true, false);
    }

    public EnhanceException(String message) {
        this(message, null);
    }

    public EnhanceException(Throwable cause) {
        this(cause.getMessage(), cause);
    }
}
