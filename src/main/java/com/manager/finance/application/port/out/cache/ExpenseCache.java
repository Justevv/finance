package com.manager.finance.application.port.out.cache;

import com.manager.finance.domain.model.ExpenseModel;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseCache {

    Optional<ExpenseModel> findByIdAndUserId(UUID id, UUID userId);

    ExpenseModel save(ExpenseModel expenseResponseDTO);
}
