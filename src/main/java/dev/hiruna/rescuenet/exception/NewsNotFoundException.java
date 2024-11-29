package dev.hiruna.rescuenet.exception;

public class NewsNotFoundException extends RuntimeException {

    // Constructor without parameters, default message
    public NewsNotFoundException() {
        super("News article not found.");
    }

    // Constructor with custom message
    public NewsNotFoundException(String message) {
        super(message);
    }

    // Constructor with custom message and cause
    public NewsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause
    public NewsNotFoundException(Throwable cause) {
        super(cause);
    }
}