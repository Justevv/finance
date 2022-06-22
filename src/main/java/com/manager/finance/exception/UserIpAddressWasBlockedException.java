package com.manager.finance.exception;

public class UserIpAddressWasBlockedException extends RuntimeException {
    public UserIpAddressWasBlockedException(String message) {
        super(message);
    }
}
