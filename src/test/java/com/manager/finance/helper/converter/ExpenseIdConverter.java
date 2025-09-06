package com.manager.finance.helper.converter;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.ExpenseEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata.ExpenseSpringDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

@TestConfiguration
public class ExpenseIdConverter implements Converter<String, ExpenseEntity> {
    @Autowired
    private ExpenseSpringDataRepository expenseRepository;

    @Override
    public ExpenseEntity convert(String source) {
        return expenseRepository.findById(UUID.fromString(source)).orElseThrow();
    }
}
