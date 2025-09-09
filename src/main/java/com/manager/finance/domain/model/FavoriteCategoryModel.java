package com.manager.finance.domain.model;

import com.manager.user.domain.model.UserModel;
import lombok.Builder;

import java.util.UUID;

@Builder
public record FavoriteCategoryModel(
        UUID id,
        CategoryModel category,
        UserModel user
) implements Model {
}