package com.manager.user.infrastructure.adapter.out.persistence.exception;

import java.util.UUID;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(UUID id) {
        super(String.format("role with id [%s] is not found", id.toString()));
    }
}
