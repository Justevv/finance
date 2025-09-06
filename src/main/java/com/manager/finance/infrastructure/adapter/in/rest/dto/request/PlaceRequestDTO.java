package com.manager.finance.infrastructure.adapter.in.rest.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PlaceRequestDTO(
        UUID id,
        String name,
        String address
) implements RequestDTO {
}
