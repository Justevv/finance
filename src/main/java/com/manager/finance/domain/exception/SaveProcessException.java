package com.manager.finance.domain.exception;

public class SaveProcessException extends RuntimeException {
    public SaveProcessException(Object entity) {
        super(String.format("Exception during save entity from model %s", entity));
    }
}
