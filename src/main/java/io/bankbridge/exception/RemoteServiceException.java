package io.bankbridge.exception;

public class RemoteServiceException extends CustomException {

    public RemoteServiceException(String message) {
        super(message);
    }

    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ErrorResponse format() {
        return new ErrorResponse(500, "Failed to connect with endpoint bank server. Try again");
    }
}
