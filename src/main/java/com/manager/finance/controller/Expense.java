package com.manager.finance.controller;

import com.manager.finance.dto.ExpenseDTO;
import com.manager.finance.dto.response.ExpenseResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.service.ExpenseService;
import com.manager.finance.metric.TrackExecutionTime;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/expense")
@Slf4j
public class Expense extends CrudApiResponse<ExpenseDTO, ExpenseResponseDTO> {
    private final ExpenseService expenseService;

    public Expense(ExpenseService expenseService) {
        super(expenseService);
        this.expenseService = expenseService;
    }

    @GetMapping("/page/{page}")
    @TrackExecutionTime
    public List<ExpenseResponseDTO> getExpensesPage(Principal principal, @PathVariable("page") int page,
                                                    @RequestParam(defaultValue = "500") int countPerPage) {
        log.debug("Input filter {}, search {}", page, countPerPage);
        return expenseService.getAll(page, countPerPage, principal);
    }

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<Object> getExpenses(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> getExpense(Principal principal, @PathVariable("id") String guid) {
        return get(guid, principal);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<Object> addExpense(Principal principal, @RequestBody @Valid ExpenseDTO expenseDTO, BindingResult bindingResult) {
        return create(principal, expenseDTO, bindingResult);
    }

    @PatchMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> changeExpenseProperty(Principal principal, @PathVariable("id") String guid,
                                                        @RequestBody @Valid ExpenseDTO expenseDTO, BindingResult bindingResult) {
        return update(guid, principal, expenseDTO, bindingResult);
    }

    @PutMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> changeExpense(Principal principal, @PathVariable("id") String guid, @RequestBody @Valid ExpenseDTO expenseDTO,
                                                BindingResult bindingResult) {
        return update(guid, principal, expenseDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> deleteExpense(Principal principal, @PathVariable("id") String guid) {
        return delete(guid, principal);
    }

    @GetMapping("sum")
    @TrackExecutionTime
    public double getCategoryRepo(Principal principal) {
        return expenseService.getSum(principal);
    }

    @GetMapping("sum/{groupId}")
    @TrackExecutionTime
    public double getCategoryRepo(Principal principal, @PathVariable("groupId") CategoryEntity categoryEntity) {
        return expenseService.getSum(principal, categoryEntity);
    }
}

