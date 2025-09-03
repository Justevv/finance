package com.manager.finance.helper.prepare;

import com.manager.finance.infrastructure.persistace.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.UUID;

@TestConfiguration
public class CategoryPrepareHelper {
    @Autowired
    private UserPrepareHelper userPrepareHelper;

    public CategoryEntity createCategory() {
        var categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.randomUUID());
        categoryEntity.setName("categoryName");
        return categoryEntity;
    }
}

