package dev.hiruna.rescuenet.exception;

public class ResourceNotFoundException extends RuntimeException {

    // Constructor without parameters, default message
    public ResourceNotFoundException() {
        super("Resource not found.");
    }

    // Constructor with custom message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor with custom message and cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}