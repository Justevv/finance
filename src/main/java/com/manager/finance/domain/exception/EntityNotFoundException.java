package com.manager.finance.domain.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String type, UUID id) {
        super(String.format("%s with id [%s] is not found in database", type, id));
    }
}
