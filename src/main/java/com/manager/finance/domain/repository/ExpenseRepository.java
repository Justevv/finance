package com.manager.finance.domain.repository;

import com.manager.finance.infrastructure.persistace.entity.CategoryEntity;
import com.manager.finance.infrastructure.persistace.entity.ExpenseEntity;
import com.manager.user.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository {
    List<ExpenseEntity> findByUser(UserEntity userEntity);

    Optional<ExpenseEntity> findByIdAndUser(UUID id, UserEntity userEntity);

    boolean existsByIdAndUser(UUID id, UserEntity userEntity);

    List<ExpenseEntity> findByUser(UserEntity userEntity, Pageable pageable);

    void deleteById(UUID id, UserEntity user);

    double selectSum(Principal principal);

    double selectSum(Principal principal, CategoryEntity categoryEntity);

    ExpenseEntity save(ExpenseEntity expense);
}
