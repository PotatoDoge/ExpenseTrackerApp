package com.expensetrackerapp.shared.exceptions;

public class DatabaseInteractionException extends RuntimeException {
    public DatabaseInteractionException(String message) {
        super(message);
    }
}
