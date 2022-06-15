package com.manager.finance.model;

import com.manager.finance.config.CrudLogConstants;
import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.repository.CategoryRepository;
import com.manager.finance.repository.ExpenseRepository;
import com.manager.finance.repository.PlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ExpenseModel extends CrudModel<ExpenseEntity, ExpenseDTO> {
    private static final String EXPENSE = "expense";
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceRepository placeRepository;
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(EXPENSE);

    public ExpenseModel(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, PlaceRepository placeRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.placeRepository = placeRepository;
    }

    @Cacheable(cacheNames = "expense")
    public List<ExpenseEntity> getAll(int page, int count, Principal principal) {
        var user = getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.debug("Input start page is {}, count on page is {}", page, count);
        Pageable pageable = PageRequest.of(page, count);
        var expenseEntities = expenseRepository.findByUser(user, pageable);
        log.debug(crudLogConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    public List<ExpenseEntity> getAll(Principal principal) {
        var user = getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var expenseEntities = expenseRepository.findByUser(user);
        log.debug(crudLogConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    @Override
    public ExpenseEntity create(ExpenseDTO expenseDTO, Principal principal) {
        log.debug(crudLogConstants.getInputDataNew(), expenseDTO);
        var expense = getMapper().map(expenseDTO, ExpenseEntity.class);
        expense.setUser(getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        expense.setDate(LocalDateTime.now());
        setDefaultValue(expense);
        expenseRepository.save(expense);
        log.info(crudLogConstants.getSaveToDatabase(), expense);
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
        log.debug(crudLogConstants.getInputDataToChange(), expenseDTO, expense);
        getMapper().map(expenseDTO, expense);
        setDefaultValue(expense);
        expenseRepository.save(expense);
        log.info(crudLogConstants.getUpdatedToDatabase(), expense);
        return expense;
    }

    @Override
    public Void delete(ExpenseEntity expenseEntity) {
        log.debug(crudLogConstants.getInputDataForDelete(), expenseEntity);
        expenseRepository.delete(expenseEntity);
        log.info(crudLogConstants.getDeletedFromDatabase(), expenseEntity);
        return null;
    }

    public double getSum() {
        return expenseRepository.selectSum();
    }

    public double getSum(CategoryEntity categoryEntity) {
        return expenseRepository.selectSum(categoryEntity);
    }
}


