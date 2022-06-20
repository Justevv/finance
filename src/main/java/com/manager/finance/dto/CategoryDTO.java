package com.manager.finance.dto;

import com.manager.finance.entity.CategoryEntity;
import lombok.Data;

@Data
public class CategoryDTO implements CrudDTO {
    private String name;
    private CategoryEntity parentCategory;

}
