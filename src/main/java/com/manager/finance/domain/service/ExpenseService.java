package com.manager.finance.domain.service;

import com.manager.finance.application.port.in.ExpenseUseCase;
import com.manager.finance.application.port.out.cache.ExpenseCache;
import com.manager.finance.application.port.out.repository.ExpenseRepository;
import com.manager.finance.domain.exception.EntityNotFoundException;
import com.manager.finance.domain.exception.SaveProcessException;
import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.log.LogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import com.manager.user.helper.UserHelper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpenseService implements ExpenseUseCase {
    private static final String ENTITY_TYPE_NAME = "expense";
    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final PlaceService placeService;
    private final UserHelper userHelper;
    private final ExpenseCache expenseCache;
    private CrudLogConstants crudLogConstants;

    @PostConstruct
    private void init() {
        crudLogConstants = new CrudLogConstants(ENTITY_TYPE_NAME);
    }

    @TrackExecutionTime
    @Override
    public ExpenseModel get(UUID id, UUID userId) {
        var expense = expenseCache.findByIdAndUserId(id, userId);
        ExpenseModel expenseModel = null;
        if (expense.isPresent()) {
            expenseModel = expense.get();
            log.debug(crudLogConstants.getGetDTOFromCache(), expenseModel);
        } else {
            expenseModel = expenseRepository.getByIdAndUser(id, userId);
            expenseCache.save(expenseModel);
            log.debug(crudLogConstants.getLoadEntityFromDatabase(), expenseModel);
            log.debug(crudLogConstants.getSaveDTOToCache(), expenseModel);

        }
        return expenseModel;
    }

    @TrackExecutionTime
    @Override
    public List<ExpenseModel> getAll(int page, int countPerPage, UUID userId) {
        log.debug("Input start page is {}, count on page is {}", page, countPerPage);
        Pageable pageable = PageRequest.of(page, countPerPage);
        var expenseEntities = expenseRepository.findByUser(userId, pageable);
        log.debug(crudLogConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }


    @TrackExecutionTime
    public List<ExpenseModel> getAll(UUID userId) {
        var expenseEntities = expenseRepository.findByUser(userId);
        log.debug(crudLogConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    @TrackExecutionTime
    @Transactional
    @Override
    public ExpenseModel create(UUID userId, ExpenseModel expenseModel) {
        log.debug(crudLogConstants.getInputNewDTO(), expenseModel);
        var categoryModel = categoryService.getOrCreate(userId, expenseModel.category());
        var placeModel = placeService.getOrCreate(expenseModel.place());
        var save = ExpenseModel.builder()
                .id(UUID.randomUUID())
                .description(expenseModel.description())
                .date(LocalDateTime.now())
                .category(categoryModel)
                .place(placeModel)
                .paymentType(expenseModel.paymentType())
                .amount(expenseModel.amount())
                .account(expenseModel.account())
                .transactionType(expenseModel.transactionType())
                .userId(userId)
                .build();
        var saved = expenseRepository.save(save);
        if (saved == null) {
            throw new SaveProcessException(save);
        }
        log.info(crudLogConstants.getSaveEntityToDatabase(), saved);
        expenseCache.save(saved);
        return saved;
    }

    @TrackExecutionTime
    @Transactional
    @Override
    public ExpenseModel update(UUID id, UUID userId, ExpenseModel input) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), input, null);
        var current = expenseRepository.getByIdAndUser(id, userId);

        var save = ExpenseModel.builder()
                .id(current.id())
                .description(input.description())
                .date(current.date())
                .category(categoryService.getOrCreate(userId, input.category()))
                .place(placeService.getOrCreate(input.place()))
                .paymentType(input.paymentType())
                .amount(input.amount())
                .account(input.account())
                .transactionType(input.transactionType())
                .userId(current.userId())
                .build();
        var saved = expenseRepository.save(save);
        if (saved == null) {
            throw new SaveProcessException(save);
        }
        log.info(crudLogConstants.getSaveEntityToDatabase(), saved);
        return saved;
    }

    @TrackExecutionTime
    @Transactional
    @Override
    public ExpenseModel patch(UUID id, UUID userId, ExpenseModel input) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), input, null);
        var current = expenseRepository.getByIdAndUser(id, userId);

        var save = ExpenseModel.builder()
                .id(current.id())
                .description(input.description() != null ? input.description() : current.description())
                .date(current.date())
                .category(categoryService.getOrCreate(userId, input.category()))
                .place(placeService.getOrCreate(input.place()))
                .paymentType(input.paymentType() != null ? input.paymentType() : current.paymentType())
                .amount(input.amount() != null ? input.amount() : current.amount())
                .account(input.account() != null ? input.account() : current.account())
                .transactionType(input.transactionType() != null ? input.transactionType() : current.transactionType())
                .userId(current.userId())
                .build();
        var saved = expenseRepository.save(save);
        if (saved == null) {
            throw new SaveProcessException(save);
        }
        log.info(crudLogConstants.getSaveEntityToDatabase(), saved);
        return saved;
    }

    @TrackExecutionTime
    @Transactional
    @Override
    public void delete(UUID id, UUID userId) {
        getOrThrow(id, userId);
        log.debug(LogConstants.DELETE_ENTITY_FROM_DATABASE_BY_ID, ENTITY_TYPE_NAME, id);
        expenseRepository.deleteById(id);
    }

    @TrackExecutionTime
    @Override
    public double getSum(UUID userId) {
        return expenseRepository.selectSum(userId);
    }

    @TrackExecutionTime
    @Override
    public double getSum(UUID userId, CategoryEntity categoryEntity) {
        return expenseRepository.selectSum(userId, categoryEntity);
    }

    @TrackExecutionTime
    @Override
    public void checkExpense(UUID id, UUID userId) {
        var exist = expenseRepository.existsByIdAndUser(id, userId);
        if (!exist) {
            throw new EntityNotFoundException(ENTITY_TYPE_NAME, id);
        }
    }

    private void getOrThrow(UUID id, UUID userId) {
        var exist = expenseRepository.existsByIdAndUser(id, userId);
        if (!exist) {
            throw new EntityNotFoundException(ENTITY_TYPE_NAME, id);
        }
    }

}


