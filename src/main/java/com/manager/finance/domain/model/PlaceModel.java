package com.manager.finance.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PlaceModel(
        UUID id,
        String name,
        String address
) implements Model {
}