package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.repository.CategoryRepository;
import com.manager.finance.repository.ExpenseRepository;
import com.manager.finance.repository.PlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExpenseModel extends CrudModel<ExpenseEntity, ExpenseDTO> {
    private static final String EXPENSE = "expense";
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceRepository placeRepository;
    private final LogConstants logConstants = new LogConstants(EXPENSE);

    public ExpenseModel(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, PlaceRepository placeRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.placeRepository = placeRepository;
    }

    @Cacheable(cacheNames = "expense")
    public List<ExpenseEntity> getExpense(long startWith, long count, Principal principal) {
        log.debug("Input filter {}, search {}", startWith, count);
        List<ExpenseEntity> expenseEntities = new ArrayList<>();
        for (; startWith < count; startWith++) {
            expenseRepository.findById(startWith).ifPresent(expenseEntities::add);
        }
        log.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    public List<ExpenseEntity> getExpense() {
        var expenseEntities = expenseRepository.findAll();
        log.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    @Override
    public ExpenseEntity create(ExpenseDTO expenseDTO, Principal principal) {
        log.debug(logConstants.getInputDataNew(), expenseDTO);
        var expense = getMapper().map(expenseDTO, ExpenseEntity.class);
        expense.setUser(getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        expense.setDate(LocalDateTime.now());
        setDefaultValue(expense);
        expenseRepository.save(expense);
        log.info(logConstants.getSaveToDatabase(), expense);
        return expense;
    }

    private void setDefaultValue(ExpenseEntity expense) {
        if (expense.getCategory() == null) {
            expense.setCategory(categoryRepository.findByName("Default"));
        }
        if (expense.getPlace() == null) {
            expense.setPlace(placeRepository.findByName("Undefined"));
        }
    }

    @Override
    public ExpenseEntity update(ExpenseEntity expense, ExpenseDTO expenseDTO) {
        log.debug(logConstants.getInputDataToChange(), expenseDTO, expense);
        getMapper().map(expenseDTO, expense);
        setDefaultValue(expense);
        expenseRepository.save(expense);
        log.info(logConstants.getUpdatedToDatabase(), expense);
        return expense;
    }

    @Override
    public Void delete(ExpenseEntity expenseEntity) {
        log.debug(logConstants.getInputDataForDelete(), expenseEntity);
        expenseRepository.delete(expenseEntity);
        log.info(logConstants.getDeletedFromDatabase(), expenseEntity);
        return null;
    }

    public double getSum() {
        return expenseRepository.selectSum();
    }

    public double getSum(CategoryEntity categoryEntity) {
        return expenseRepository.selectSum(categoryEntity);
    }
}


