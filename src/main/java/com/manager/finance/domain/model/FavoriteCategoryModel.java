package com.manager.finance.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FavoriteCategoryModel(
        UUID id,
        CategoryModel category,
        UserModel user
) implements Model {
}