package dev.hiruna.rescuenet.exception;

public class CommentNotFoundException extends RuntimeException {

    // Constructor without parameters, default message
    public CommentNotFoundException() {
        super("Comment not found.");
    }

    // Constructor with custom message
    public CommentNotFoundException(String message) {
        super(message);
    }

    // Constructor with custom message and cause
    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause
    public CommentNotFoundException(Throwable cause) {
        super(cause);
    }
}