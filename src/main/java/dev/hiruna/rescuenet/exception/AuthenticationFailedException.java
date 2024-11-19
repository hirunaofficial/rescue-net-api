package dev.hiruna.rescuenet.exception;

public class AuthenticationFailedException extends RuntimeException {

    // Constructor without parameters
    public AuthenticationFailedException() {
        super("Authentication failed.");
    }

    // Constructor with custom message
    public AuthenticationFailedException(String message) {
        super(message);
    }

    // Constructor with custom message and cause
    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause
    public AuthenticationFailedException(Throwable cause) {
        super(cause);
    }
}