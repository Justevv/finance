package com.manager.finance.controller;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.model.ExpenseModel;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class Expense {
    private static final String EXPENSE = "expense";
    private final LogConstants logConstants = new LogConstants(EXPENSE);
    private final ExpenseModel expenseModel;

    public Expense(ExpenseModel expenseModel) {
        this.expenseModel = expenseModel;
    }

    @GetMapping
    public List<ExpenseEntity> getExpense(@RequestParam(defaultValue = "0") long startWith,
                                          @RequestParam(defaultValue = "500") long count) {
        log.debug("Input filter {}, search {}", startWith, count);
        List<ExpenseEntity> expenseEntities = expenseModel.getExpense(startWith, count);
        log.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    @GetMapping("{id}")
    public ExpenseEntity getExpense(@PathVariable("id") ExpenseEntity expenseEntity) {
        log.debug(logConstants.getInput(), expenseEntity);
        return expenseEntity;
    }

    @PostMapping
    public ResponseEntity addExpense(@Valid ExpenseDTO expenseDTO, BindingResult bindingResult) throws IOException {
        log.debug(logConstants.getInputDataNew(), expenseDTO);
        ResponseEntity responseEntity;

        if (!bindingResult.hasErrors()) {
            ExpenseEntity expense = expenseModel.addExpense(expenseDTO);
            log.debug(logConstants.getSaveToDatabase(), expense);
            responseEntity = ResponseEntity.ok(expense);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    @PatchMapping("{id}")
    public ResponseEntity changeExpenseProperty(@PathVariable("id") ExpenseEntity expense, @Valid ExpenseDTO expenseDTO,
                                                BindingResult bindingResult) {
        log.debug(logConstants.getInputDataToChange(), expenseDTO, expense);
        ResponseEntity responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(expenseModel.changeExpense(expense, expenseDTO));
            log.debug(logConstants.getSaveToDatabase(), expenseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    @PutMapping("{id}")
    public ResponseEntity changeExpense(@PathVariable("id") ExpenseEntity expense, @Valid ExpenseDTO expenseDTO,
                                        BindingResult bindingResult) {
        log.debug(logConstants.getInputDataToChange(), expense, expenseDTO);
        ResponseEntity responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(expenseModel.changeExpense(expense, expenseDTO));
            log.debug(logConstants.getSaveToDatabase(), expenseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteExpense(@PathVariable("id") ExpenseEntity expense) {
        log.debug(logConstants.getInputDataForDelete(), expense);
        ResponseEntity responseEntity = ResponseEntity.ok(expenseModel.deleteExpense(expense));
        log.debug(logConstants.getDeletedResponse(), responseEntity);
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

