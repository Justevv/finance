package com.manager.finance.infrastructure.adapter.in.exception;

public class InvalidUUIDException extends RuntimeException {
    public InvalidUUIDException(String id, String type) {
        super(String.format("Invalid UUID [%s] for %s", id, type));
    }
}
