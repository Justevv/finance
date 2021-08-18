package com.manager.controller;

import com.manager.config.LogConstants;
import com.manager.dto.ExpenseDTO;
import com.manager.entity.CategoryEntity;
import com.manager.entity.ExpenseEntity;
import com.manager.model.ExpenseModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expense")
public class Expense {
    private static final Logger LOGGER = LogManager.getLogger(Expense.class);
    private static final String EXPENSE = "expense";
    private final LogConstants logConstants = new LogConstants(EXPENSE);
    private final ExpenseModel expenseModel;

    public Expense(ExpenseModel expenseModel) {
        this.expenseModel = expenseModel;
    }

    @GetMapping
    public List<ExpenseEntity> getExpense(@RequestParam(defaultValue = "0") long startWith,
                                          @RequestParam(defaultValue = "500") long count) {
        LOGGER.debug("Input filter {}, search {}", startWith, count);
        List<ExpenseEntity> expenseEntities = expenseModel.getExpense(startWith, count);
        LOGGER.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    @GetMapping("{id}")
    public ExpenseEntity getExpense(@PathVariable("id") ExpenseEntity expenseEntity) {
        LOGGER.debug(logConstants.getInput(), expenseEntity);
        return expenseEntity;
    }

    @PostMapping
    public ResponseEntity addExpense(@Valid ExpenseDTO expenseDTO, BindingResult bindingResult) throws IOException {
        LOGGER.debug(logConstants.getInputDataNew(), expenseDTO);
        ResponseEntity responseEntity;

        if (!bindingResult.hasErrors()) {
            ExpenseEntity expense = expenseModel.addExpense(expenseDTO);
            LOGGER.debug(logConstants.getSaveToDatabase(), expense);
            responseEntity = ResponseEntity.ok(expense);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            LOGGER.debug(logConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        LOGGER.debug(logConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    @PatchMapping("{id}")
    public ResponseEntity changeExpenseProperty(@PathVariable("id") ExpenseEntity expense, @Valid ExpenseDTO expenseDTO,
                                                BindingResult bindingResult) {
        LOGGER.debug(logConstants.getInputDataToChange(), expenseDTO, expense);
        ResponseEntity responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(expenseModel.changeExpense(expense, expenseDTO));
            LOGGER.debug(logConstants.getSaveToDatabase(), expenseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            LOGGER.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        LOGGER.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    @PutMapping("{id}")
    public ResponseEntity changeExpense(@PathVariable("id") ExpenseEntity expense, @Valid ExpenseDTO expenseDTO,
                                        BindingResult bindingResult) {
        LOGGER.debug(logConstants.getInputDataToChange(), expense, expenseDTO);
        ResponseEntity responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(expenseModel.changeExpense(expense, expenseDTO));
            LOGGER.debug(logConstants.getSaveToDatabase(), expenseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            LOGGER.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        LOGGER.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteExpense(@PathVariable("id") ExpenseEntity expense) {
        LOGGER.debug(logConstants.getInputDataForDelete(), expense);
        ResponseEntity responseEntity = ResponseEntity.ok(expenseModel.deleteExpense(expense));
        LOGGER.debug(logConstants.getDeletedResponse(), responseEntity);
        return responseEntity;
    }

    @GetMapping("sum")
    public double getCategoryRepo() {
        return expenseModel.getSum();
    }

    @GetMapping("sum/{groupId}")
    public double getCategoryRepo(@PathVariable("groupId") CategoryEntity categoryEntity) {
        return expenseModel.getSum(categoryEntity);
    }
}

