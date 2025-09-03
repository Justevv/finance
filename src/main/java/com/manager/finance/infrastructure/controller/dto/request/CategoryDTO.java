package com.manager.finance.infrastructure.controller.dto.request;

import com.manager.finance.infrastructure.persistace.entity.CategoryEntity;
import lombok.Data;

@Data
public class CategoryDTO extends BaseCrudDTO {
    private String name;
    private CategoryEntity parentCategory;

}
