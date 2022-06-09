package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.repo.CategoryRepo;
import com.manager.finance.repo.ExpenseRepo;
import com.manager.finance.repo.PlaceRepo;
import com.manager.finance.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExpenseModel implements CrudModel<ExpenseEntity, ExpenseDTO> {
    private static final String EXPENSE = "expense";
    private final ExpenseRepo expenseRepo;
    private final CategoryRepo categoryRepo;
    private final PlaceRepo placeRepo;
    private final UserRepo userRepo;
    private final LogConstants logConstants = new LogConstants(EXPENSE);

    public ExpenseModel(ExpenseRepo expenseRepo, CategoryRepo categoryRepo, PlaceRepo placeRepo, UserRepo userRepo) {
        this.expenseRepo = expenseRepo;
        this.categoryRepo = categoryRepo;
        this.placeRepo = placeRepo;
        this.userRepo = userRepo;
    }

    @Cacheable(cacheNames = "expense")
    public List<ExpenseEntity> getExpense(long startWith, long count) {
        log.debug("Input filter {}, search {}", startWith, count);
        List<ExpenseEntity> expenseEntities = new ArrayList<>();
        for (; startWith < count; startWith++) {
            expenseRepo.findById(startWith).ifPresent(expenseEntities::add);
        }
        log.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    public List<ExpenseEntity> getExpense() {
        List<ExpenseEntity> expenseEntities = expenseRepo.findAll();
        log.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    @Override
    public ExpenseEntity create(ExpenseDTO expenseDTO){
        log.debug(logConstants.getInputDataNew(), expenseDTO);
//        expenseDTO.setUser(userRepo.findByUsername(principal.getName()));
        ExpenseEntity expenseEntity = new ExpenseEntity(expenseDTO);
        expenseEntity.setCategory(expenseDTO.getCategory() != null
                ? expenseDTO.getCategory() : categoryRepo.findByName("Default"));
        expenseEntity.setPlace(expenseDTO.getPlace() != null ? expenseDTO.getPlace() : placeRepo.findByName("Undefined"));
        expenseRepo.save(expenseEntity);
        log.info(logConstants.getSaveToDatabase(), expenseEntity);
        return expenseEntity;
    }

    @Override
    public ExpenseEntity update(ExpenseEntity expense, ExpenseDTO expenseDTO) {
        log.debug(logConstants.getInputDataToChange(), expenseDTO, expense);
        BeanUtils.copyProperties(expenseDTO, expense);
        expense.setCategory(expenseDTO.getCategory() != null
                ? expenseDTO.getCategory() : categoryRepo.findByName("Default"));
        expense.setPlace(expenseDTO.getPlace() != null ? expenseDTO.getPlace() : placeRepo.findByName("Undefined"));
        expenseRepo.save(expense);
        log.info(logConstants.getUpdatedToDatabase(), expense);
        return expense;
    }
    @Override
    public Void delete(ExpenseEntity expenseEntity) {
        log.debug(logConstants.getInputDataForDelete(), expenseEntity);
        expenseRepo.delete(expenseEntity);
        log.info(logConstants.getDeletedFromDatabase(), expenseEntity);
        return null;
    }

    public double getSum() {
        return expenseRepo.selectSum();
    }

    public double getSum(CategoryEntity categoryEntity) {
        return expenseRepo.selectSum(categoryEntity);
    }
}


