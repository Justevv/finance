package com.manager.finance.helper.prepare;

import com.manager.finance.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class CategoryPrepareHelper {
    @Autowired
    private UserPrepareHelper userPrepareHelper;

    public CategoryEntity createCategory() {
        var categoryEntity = new CategoryEntity();
        categoryEntity.setId(1);
        categoryEntity.setName("categoryName");
        categoryEntity.setUser(userPrepareHelper.createUser());
        return categoryEntity;
    }
}

