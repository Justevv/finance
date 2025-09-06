package com.manager.finance.helper.converter;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata.CategorySpringDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

@TestConfiguration
public class CategoryIdConverter implements Converter<String, CategoryEntity> {
    @Autowired
    private CategorySpringDataRepository categoryRepository;

    @Override
    public CategoryEntity convert(String source) {
        return categoryRepository.findById(UUID.fromString(source)).orElseThrow();
    }
}
