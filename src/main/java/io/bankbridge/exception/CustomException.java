package io.bankbridge.exception;

public abstract class CustomException extends RuntimeException implements ErrorFormatter {
    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
