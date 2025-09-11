package com.manager.user.domain.exception;

public class UserIpAddressWasBlockedException extends RuntimeException {
    public UserIpAddressWasBlockedException(String message) {
        super(message);
    }
}
