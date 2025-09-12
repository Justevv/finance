package com.manager.finance.application.port.out.repository;

import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository {
    List<ExpenseModel> findByUser(UUID userId);

    ExpenseModel getByIdAndUser(UUID id, UUID userId);

    boolean existsByIdAndUser(UUID id, UUID userId);

    List<ExpenseModel> findByUser(UUID userId, Pageable pageable);

    void deleteById(UUID id);

    double selectSum(UUID userId);

    double selectSum(UUID userId, CategoryEntity categoryEntity);

    ExpenseModel save(ExpenseModel expense);
}
