package com.manager.finance.controller;

import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.dto.response.ExpenseResponseDTO;
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
public class Expense extends CrudApiResponse<ExpenseModel, ExpenseEntity, ExpenseDTO, ExpenseResponseDTO> {
    private static final String EXPENSE_MODEL_TYPE = "expense";
    private final ExpenseModel expenseModel;

    public Expense(ExpenseModel expenseModel) {
        super(expenseModel, EXPENSE_MODEL_TYPE);
        this.expenseModel = expenseModel;
    }

    @GetMapping("/page/{page}")
    public List<ExpenseResponseDTO> getExpensesPage(Principal principal, @PathVariable("page") int page,
                                       @RequestParam(defaultValue = "500") int countPerPage) {
        log.debug("Input filter {}, search {}", page, countPerPage);
        return expenseModel.getAll(page, countPerPage, principal);
    }

    @GetMapping
    public ResponseEntity<Object> getExpenses(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getExpense(Principal principal, @PathVariable("id") ExpenseEntity expenseEntity) {
        return get(principal, expenseEntity);
    }

    @PostMapping
    public ResponseEntity<Object> addExpense(Principal principal, @Valid ExpenseDTO expenseDTO,
                                             BindingResult bindingResult) throws UserPrincipalNotFoundException {
        return create(principal, expenseDTO, bindingResult);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> changeExpenseProperty(Principal principal, @PathVariable("id") ExpenseEntity expense,
                                                        @Valid ExpenseDTO expenseDTO, BindingResult bindingResult) {
        return update(principal, expense, expenseDTO, bindingResult);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> changeExpense(Principal principal, @PathVariable("id") ExpenseEntity expense, @Valid ExpenseDTO expenseDTO,
                                                BindingResult bindingResult) {
        return update(principal, expense, expenseDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteExpense(Principal principal, @PathVariable("id") ExpenseEntity expense) {
        return delete(principal, expense);
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

