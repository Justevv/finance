package com.manager.user.domain.exception;


public class PasswordResetTokenNotFoundException extends RuntimeException {
    public PasswordResetTokenNotFoundException(String message) {
        super(message);
    }
}
