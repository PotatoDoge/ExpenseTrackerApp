package com.expensetrackerapp.shared.exceptions;

public class NotFoundInDatabase extends RuntimeException {
    public NotFoundInDatabase(String message) {
        super(message);
    }
}
