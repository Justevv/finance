package com.manager.finance.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String type, UUID guid) {
        super(String.format("%s with guid [%s] is not found in database", type, guid));
    }
}
