package com.manager.finance.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserModel(
        UUID id,
        String username
) implements Model {
}
