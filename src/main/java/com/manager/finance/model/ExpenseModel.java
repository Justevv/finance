package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.repo.CategoryRepo;
import com.manager.finance.repo.ExpenseRepo;
import com.manager.finance.repo.PlaceRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseModel {
    private static final Logger LOGGER = LogManager.getLogger(ExpenseModel.class);
    private static final String EXPENSE = "expense";
    private final ExpenseRepo expenseRepo;
    private final CategoryRepo categoryRepo;
    private final PlaceRepo placeRepo;
    private final LogConstants logConstants = new LogConstants(EXPENSE);

    public ExpenseModel(ExpenseRepo expenseRepo, CategoryRepo categoryRepo, PlaceRepo placeRepo) {
        this.expenseRepo = expenseRepo;
        this.categoryRepo = categoryRepo;
        this.placeRepo = placeRepo;
    }

    @Cacheable(cacheNames = "expense")
    public List<ExpenseEntity> getExpense(long startWith, long count) {
        LOGGER.debug("Input filter {}, search {}", startWith, count);
        List<ExpenseEntity> expenseEntities = new ArrayList<>();
        for (; startWith < count; startWith++) {
            expenseRepo.findById(startWith).ifPresent(expenseEntities::add);
        }
        LOGGER.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    public List<ExpenseEntity> getExpense() {
        List<ExpenseEntity> expenseEntities = expenseRepo.findAll();
        LOGGER.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    public ExpenseEntity addExpense(ExpenseDTO expenseDTO) throws IOException {
        LOGGER.debug(logConstants.getInputDataNew(), expenseDTO);
        ExpenseEntity expenseEntity = new ExpenseEntity(expenseDTO);
        expenseEntity.setCategory(expenseDTO.getCategory() != null
                ? expenseDTO.getCategory() : categoryRepo.findByName("Default"));
        expenseEntity.setPlace(expenseDTO.getPlace() != null ? expenseDTO.getPlace() : placeRepo.findByName("Undefined"));
        expenseRepo.save(expenseEntity);
        LOGGER.info(logConstants.getSaveToDatabase(), expenseEntity);
        return expenseEntity;
    }

    public ExpenseEntity changeExpense(long id, ExpenseDTO expenseDTO) {
        LOGGER.debug(logConstants.getInputDataToChange(), id, expenseDTO);
        ExpenseEntity expenseEntity = expenseRepo.findById(id).get();
        BeanUtils.copyProperties(expenseDTO, expenseEntity);
        expenseRepo.save(expenseEntity);
        LOGGER.info(logConstants.getUpdatedToDatabase(), expenseEntity);
        return expenseEntity;
    }

    public ExpenseEntity changeExpense(ExpenseEntity expense, ExpenseDTO expenseDTO) {
        LOGGER.debug(logConstants.getInputDataToChange(), expenseDTO, expense);
        BeanUtils.copyProperties(expenseDTO, expense);
        expense.setCategory(expenseDTO.getCategory() != null
                ? expenseDTO.getCategory() : categoryRepo.findByName("Default"));
        expense.setPlace(expenseDTO.getPlace() != null ? expenseDTO.getPlace() : placeRepo.findByName("Undefined"));
        expenseRepo.save(expense);
        LOGGER.info(logConstants.getUpdatedToDatabase(), expense);
        return expense;
    }

    public Void deleteExpense(ExpenseEntity expenseEntity) {
        LOGGER.debug(logConstants.getInputDataForDelete(), expenseEntity);
        expenseRepo.delete(expenseEntity);
        LOGGER.info(logConstants.getDeletedFromDatabase(), expenseEntity);
        return null;
    }

    public double getSum() {
        return expenseRepo.selectSum();
    }

    public double getSum(CategoryEntity categoryEntity) {
        return expenseRepo.selectSum(categoryEntity);
    }
}


