package com.manager.finance.helper.prepare;

import com.manager.finance.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.UUID;

@TestConfiguration
public class CategoryPrepareHelper {
    @Autowired
    private UserPrepareHelper userPrepareHelper;

    public CategoryEntity createCategory() {
        var categoryEntity = new CategoryEntity();
        categoryEntity.setGuid(UUID.randomUUID());
        categoryEntity.setName("categoryName");
        categoryEntity.setUser(userPrepareHelper.createUser());
        return categoryEntity;
    }
}

