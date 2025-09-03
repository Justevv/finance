package com.manager.finance.infrastructure.controller.rest;

import com.manager.finance.controller.CrudApiResponse;
import com.manager.finance.infrastructure.controller.dto.request.ExpenseRequestDTO;
import com.manager.finance.infrastructure.controller.dto.response.ExpenseResponseDTO;
import com.manager.finance.infrastructure.persistace.entity.CategoryEntity;
import com.manager.finance.application.serivice.ExpenseService;
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
public class Expense {
    private final ExpenseService expenseService;
    private final CrudApiResponse<ExpenseRequestDTO, ExpenseResponseDTO>  crudApiResponse;

    public Expense(ExpenseService expenseService, CrudApiResponse<ExpenseRequestDTO, ExpenseResponseDTO> crudApiResponse) {
        this.expenseService = expenseService;
        this.crudApiResponse = crudApiResponse;
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
        return crudApiResponse.getAll(principal);
    }

    @GetMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> getExpense(Principal principal, @PathVariable("id") String id) {
        return crudApiResponse.get(id, principal);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<Object> addExpense(Principal principal, @RequestBody @Valid ExpenseRequestDTO expenseRequestDTO, BindingResult bindingResult) {
        return crudApiResponse.create(principal, expenseRequestDTO, bindingResult);
    }

    @PatchMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> changeExpenseProperty(Principal principal, @PathVariable("id") String id,
                                                        @RequestBody @Valid ExpenseRequestDTO expenseRequestDTO, BindingResult bindingResult) {
        return crudApiResponse.update(id, principal, expenseRequestDTO, bindingResult);
    }

    @PutMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> changeExpense(Principal principal, @PathVariable("id") String id, @RequestBody @Valid ExpenseRequestDTO expenseRequestDTO,
                                                BindingResult bindingResult) {
        return crudApiResponse.update(id, principal, expenseRequestDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> deleteExpense(Principal principal, @PathVariable("id") String id) {
        return crudApiResponse.delete(id, principal);
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

