package com.sunl888.rocket.common.exception;

public class InvokerException extends RuntimeException {
    public InvokerException(String message) {
        super(message);
    }

    public InvokerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvokerException(Throwable cause) {
        super(cause);
    }
}
