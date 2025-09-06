package com.manager.finance.application.port.out.repository;

import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.user.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository {
    List<ExpenseModel> findByUser(UserEntity userEntity);

    ExpenseModel getByIdAndUser(UUID id, UserEntity userEntity);

    boolean existsByIdAndUser(UUID id, UserEntity userEntity);

    List<ExpenseModel> findByUser(UserEntity userEntity, Pageable pageable);

    void deleteById(UUID id);

    double selectSum(Principal principal);

    double selectSum(Principal principal, CategoryEntity categoryEntity);

    ExpenseModel save(ExpenseModel expense);
}
