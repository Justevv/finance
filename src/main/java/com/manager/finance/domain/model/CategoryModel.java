package com.manager.finance.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryModel(
        UUID id,
        String name,
        CategoryModel parentCategory
) implements Model {
}