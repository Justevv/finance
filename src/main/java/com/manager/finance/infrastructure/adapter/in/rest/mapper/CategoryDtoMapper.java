package com.manager.finance.infrastructure.adapter.in.rest.mapper;

import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.CategoryRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.CategoryResponseDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryDtoMapper implements DtoMapper<CategoryRequestDTO, CategoryResponseDTO, CategoryModel> {

    @Override
    public CategoryModel toModel(CategoryRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return CategoryModel.builder()
                .id(dto.id())
                .name(dto.name())
                .parentCategory(dto.parentCategory() != null ? toModel(dto.parentCategory()) : null)
                .build();
    }

    @Override
    public CategoryResponseDTO toResponseDto(CategoryModel model) {
        if (model == null) {
            return null;
        }
        return CategoryResponseDTO.builder()
                .id(model.id())
                .name(model.name())
                .parentCategory(model.parentCategory() != null ? toResponseDto(model.parentCategory()) : null)
                .build();
    }
}
