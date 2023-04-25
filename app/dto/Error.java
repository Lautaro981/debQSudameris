package dto;

public class Error {

    private String message;
    private ErrorKey errorKey;

    public Error(String message, String errorKey) {
        this.message = message;
        this.errorKey = ErrorKey.UNKNOWN_ERROR;
    }

    public String getMessage() {
        return message;
    }

    public ErrorKey getErrorKey() {
        return errorKey;
    }
}
