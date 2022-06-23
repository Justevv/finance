package com.manager.finance.exception;


public class PasswordResetTokenNotFoundException extends RuntimeException {
    public PasswordResetTokenNotFoundException(String message) {
        super(message);
    }
}
