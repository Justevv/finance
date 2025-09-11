package com.manager.user.infrastructure.adapter.out.persistence.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super(String.format("user with id [%s] is not found", id.toString()));
    }
}
