package com.manager.finance.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryResponseDTO(
        UUID id,
        String name,
        CategoryResponseDTO parentCategory
) implements ResponseDTO {
}
