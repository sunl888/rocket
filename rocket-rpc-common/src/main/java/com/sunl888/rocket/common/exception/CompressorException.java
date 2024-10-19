package com.sunl888.rocket.common.exception;

public class CompressorException extends RuntimeException {
    public CompressorException(String message) {
        super(message);
    }

    public CompressorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompressorException(Throwable cause) {
        super(cause);
    }
}
