package com.manager.finance.infrastructure.adapter.out.persistence.mapper;

import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.PlaceEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryEntityMapper implements EntityMapper<CategoryEntity, CategoryModel> {

    @Override
    public CategoryModel toModel(CategoryEntity dto) {
        if (dto == null) {
            return null;
        }
        return CategoryModel.builder()
                .id(dto.getId())
                .name(dto.getName())
                .parentCategory(dto.getParentCategory() != null ? toModel(dto.getParentCategory()) : null)
                .build();
    }

    @Override
    public CategoryEntity toEntity(CategoryModel dto) {
        if (dto == null) {
            return null;
        }
        return CategoryEntity.builder()
                .id(dto.id())
                .name(dto.name())
                .parentCategory(dto.parentCategory() != null ? toEntity(dto.parentCategory()) : null)
                .build();
    }
}
