package com.manager.finance.infrastructure.adapter.out.cache.impl;

import com.manager.finance.application.port.out.cache.ExpenseCache;
import com.manager.finance.domain.model.ExpenseModel;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ExpenseRedisRepositoryImpl implements ExpenseCache {
    @Override
    public Optional<ExpenseModel> findByIdAndUserId(UUID id, UUID userId) {
        return Optional.empty();
    }

    @Override
    public ExpenseModel save(ExpenseModel expenseResponseDTO) {
        return null;
    }
}
