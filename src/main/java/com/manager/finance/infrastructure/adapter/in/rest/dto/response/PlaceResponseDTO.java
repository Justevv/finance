package com.manager.finance.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PlaceResponseDTO(
        UUID id,
        String name,
        String address
) implements ResponseDTO {
}
