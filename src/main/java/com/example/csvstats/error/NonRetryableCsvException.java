package com.example.csvstats.error;

public class NonRetryableCsvException extends RuntimeException {
    public NonRetryableCsvException(String message, Throwable cause) {
        super(message, cause);
    }
}
