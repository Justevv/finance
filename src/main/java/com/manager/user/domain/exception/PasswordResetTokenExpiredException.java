package com.manager.user.domain.exception;


public class PasswordResetTokenExpiredException extends RuntimeException {
    public PasswordResetTokenExpiredException(String message) {
        super(message);
    }
}
