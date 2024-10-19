package com.sunl888.rocket.common.exception;

public class RetryException extends RuntimeException {

    public RetryException(String message) {
        super(message);
    }

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryException(Throwable cause) {
        super(cause);
    }
}
