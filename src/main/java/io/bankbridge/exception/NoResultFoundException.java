package io.bankbridge.exception;

public class NoResultFoundException extends CustomException {

    public NoResultFoundException(String message) {
        super(message);
    }

    @Override
    public ErrorResponse format() {
        return new ErrorResponse(404, "No result found.");
    }
}
