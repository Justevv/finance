package com.manager.finance.dto.response;

import com.manager.finance.entity.CategoryEntity;
import lombok.Data;

@Data
public class CategoryResponseDTO extends BaseCrudResponseDTO {
    private String name;
    private CategoryEntity parentCategory;

}
