package com.manager.user.exception;


public class PasswordResetTokenNotFoundException extends RuntimeException {
    public PasswordResetTokenNotFoundException(String message) {
        super(message);
    }
}
