package com.expensetrackerapp.shared.exceptions;

public class NullRequestException extends RuntimeException {
    public NullRequestException(String message) {
        super(message);
    }
}