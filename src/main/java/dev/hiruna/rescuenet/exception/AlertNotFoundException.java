package dev.hiruna.rescuenet.exception;

public class AlertNotFoundException extends RuntimeException {

    // Constructor without parameters, default message
    public AlertNotFoundException() {
        super("Alert not found.");
    }

    // Constructor with custom message
    public AlertNotFoundException(String message) {
        super(message);
    }

    // Constructor with custom message and cause
    public AlertNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause
    public AlertNotFoundException(Throwable cause) {
        super(cause);
    }
}