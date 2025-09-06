package com.manager.finance.infrastructure.adapter.out.persistence.mapper;

import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.domain.model.FavoriteCategoryModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.FavoriteCategoryEntity;
import com.manager.user.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteCategoryEntityMapper implements EntityMapper<FavoriteCategoryEntity, FavoriteCategoryModel> {
    private final EntityMapper<CategoryEntity, CategoryModel> categoryMapper;
    private final UserHelper userHelper;

    @Override
    public FavoriteCategoryModel toModel(FavoriteCategoryEntity dto) {
        if (dto == null) {
            return null;
        }
        return FavoriteCategoryModel.builder()
                .id(dto.getId())
                .category(categoryMapper.toModel(dto.getCategory()))
                .user(userHelper.toModel(dto.getUser()))
                .build();
    }

    @Override
    public FavoriteCategoryEntity toEntity(FavoriteCategoryModel dto) {
        if (dto == null) {
            return null;
        }
        return FavoriteCategoryEntity.builder()
                .id(dto.id())
                .category(categoryMapper.toEntity(dto.category()))
                .user(userHelper.toEntity(dto.user()))
                .build();
    }
}
