package dev.hiruna.rescuenet.exception;

public class PostNotFoundException extends RuntimeException {

    // Constructor without parameters, default message
    public PostNotFoundException() {
        super("Post not found.");
    }

    // Constructor with custom message
    public PostNotFoundException(String message) {
        super(message);
    }

    // Constructor with custom message and cause
    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause
    public PostNotFoundException(Throwable cause) {
        super(cause);
    }
}
