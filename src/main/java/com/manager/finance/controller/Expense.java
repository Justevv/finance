package com.manager.finance.controller;

import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.model.ExpenseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/expense")
@Slf4j
public class Expense extends CrudApiResponse<ExpenseModel, ExpenseEntity> {
    private static final String EXPENSE = "expense";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(EXPENSE);
    private final ExpenseModel expenseModel;

    public Expense(ExpenseModel expenseModel) {
        super(expenseModel, EXPENSE);
        this.expenseModel = expenseModel;
    }

    @GetMapping
    public List<ExpenseEntity> getExpense(@RequestParam(defaultValue = "0") int startWith,
                                          @RequestParam(defaultValue = "500") int count, Principal principal) {
        log.debug("Input filter {}, search {}", startWith, count);
        return expenseModel.getAll(startWith, count, principal);
    }

    @GetMapping("{id}")
    public ExpenseEntity getExpense(@PathVariable("id") ExpenseEntity expenseEntity) {
        return expenseEntity;
    }

    @PostMapping
    public ResponseEntity<Object> addExpense(@Valid ExpenseDTO expenseDTO, Principal principal,
                                             BindingResult bindingResult) throws UserPrincipalNotFoundException {
        return create(expenseDTO, principal, bindingResult);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> changeExpenseProperty(@PathVariable("id") ExpenseEntity expense,
                                                        @Valid ExpenseDTO expenseDTO, BindingResult bindingResult) {
        return update(expense, expenseDTO, bindingResult);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> changeExpense(@PathVariable("id") ExpenseEntity expense, @Valid ExpenseDTO expenseDTO,
                                                BindingResult bindingResult) {
        return update(expense, expenseDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") ExpenseEntity expense) {
        return delete(expense);
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

