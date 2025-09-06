package com.manager.finance.infrastructure.adapter.in.rest.dto.request;

import java.util.UUID;

public record CategoryRequestDTO(
        UUID id,
        String name,
        CategoryRequestDTO parentCategory
) implements RequestDTO {

}
