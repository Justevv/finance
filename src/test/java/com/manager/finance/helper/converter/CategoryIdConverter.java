package com.manager.finance.helper.converter;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

@TestConfiguration
public class CategoryIdConverter implements Converter<String, CategoryEntity> {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryEntity convert(String source) {
        return categoryRepository.findById(Long.parseLong(source)).orElseThrow();
    }
}
