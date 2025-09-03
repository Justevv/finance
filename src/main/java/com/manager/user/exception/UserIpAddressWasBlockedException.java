package com.manager.user.exception;

public class UserIpAddressWasBlockedException extends RuntimeException {
    public UserIpAddressWasBlockedException(String message) {
        super(message);
    }
}
