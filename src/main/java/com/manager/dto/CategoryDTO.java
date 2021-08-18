package com.manager.dto;

import com.manager.entity.CategoryEntity;
import lombok.Data;

@Data
public class CategoryDTO {
    private String name;
    private CategoryEntity parentCategory;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, CategoryEntity parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
    }
}
