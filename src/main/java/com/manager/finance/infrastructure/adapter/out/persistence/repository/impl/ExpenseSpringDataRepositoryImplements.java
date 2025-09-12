package com.manager.finance.infrastructure.adapter.out.persistence.repository.impl;

import com.manager.finance.application.port.out.repository.ExpenseRepository;
import com.manager.finance.domain.exception.EntityNotFoundException;
import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.ExpenseEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata.ExpenseSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ExpenseSpringDataRepositoryImplements implements ExpenseRepository {
    private static final String ENTITY_TYPE_NAME = "expense";

    private final ExpenseSpringDataRepository expenseSpringDataRepository;
    private final EntityMapper<ExpenseEntity, ExpenseModel> mapper;


    @Override
    public List<ExpenseModel> findByUser(UUID userId) {
        return expenseSpringDataRepository.findByUserId(userId).stream().map(mapper::toModel).toList();
    }

    @Override
    public ExpenseModel getByIdAndUser(UUID id, UUID userId) {
        Optional<ExpenseEntity> byIdAndUser = expenseSpringDataRepository.findByIdAndUserId(id, userId);
        if (byIdAndUser.isPresent()) {
            return byIdAndUser.map(mapper::toModel).get();
        } else {
            throw new EntityNotFoundException(ENTITY_TYPE_NAME, id);
        }
    }

    @Override
    public boolean existsByIdAndUser(UUID id, UUID userId) {
        return expenseSpringDataRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    public List<ExpenseModel> findByUser(UUID userId, Pageable pageable) {
        return expenseSpringDataRepository.findByUserId(userId, pageable).stream().map(mapper::toModel).toList();
    }

    @Override
    public void deleteById(UUID id) {
        expenseSpringDataRepository.deleteById(id);
    }

    @Override
    public double selectSum(UUID userId) {
        return 0;
    }

    @Override
    public double selectSum(UUID userId, CategoryEntity categoryEntity) {
        return 0;
    }

    @Override
    public ExpenseModel save(ExpenseModel expense) {
        ExpenseEntity save = expenseSpringDataRepository.save(mapper.toEntity(expense));
        return mapper.toModel(save);
    }


}
