package dev.hiruna.rescuenet.exception;

public class UserAlreadyExistsException extends RuntimeException {

    // Constructor without parameters
    public UserAlreadyExistsException() {
        super("User already exists.");
    }

    // Constructor with custom message
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    // Constructor with custom message and cause
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause
    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}