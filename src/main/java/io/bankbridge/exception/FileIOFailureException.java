package io.bankbridge.exception;

public class FileIOFailureException extends CustomException {

    public FileIOFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ErrorResponse format() {
        return new ErrorResponse(500, "An internal error occurred while initializing application");
    }
}
