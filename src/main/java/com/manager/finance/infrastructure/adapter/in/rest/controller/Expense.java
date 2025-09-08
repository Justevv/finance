package com.manager.finance.infrastructure.adapter.in.rest.controller;

import com.manager.finance.application.port.in.ExpenseUseCase;
import com.manager.finance.domain.exception.EntityNotFoundException;
import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestError;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestResponse;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.finance.infrastructure.adapter.in.rest.mapper.DtoMapper;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.ExpenseRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.ExpenseResponseDTO;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.helper.UserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/expense")
@Slf4j
@RequiredArgsConstructor
public class Expense {
    private final ExpenseUseCase expenseUseCase;
    //    private final CrudApiResponse<ExpenseRequestDTO, ExpenseResponseDTO, ExpenseModel> crudApiResponse;
    private final DtoMapper<ExpenseRequestDTO, ExpenseResponseDTO, ExpenseModel> expenseMapper;
    private final ErrorHelper errorHelper;
    private final UserHelper userHelper;

    @GetMapping("/page/{page}")
    @TrackExecutionTime
    public ResponseEntity<RestResponse<List<ExpenseResponseDTO>>> getExpensesPage(Principal principal, @PathVariable("page") int page,
                                                                                  @RequestParam(defaultValue = "100") int countPerPage) {
        log.debug("Input filter {}, search {}", page, countPerPage);
        var s = expenseUseCase.getAll(page, countPerPage, principal).stream().map(expenseMapper::toResponseDto).toList();
        RestResponse<List<ExpenseResponseDTO>> e = new RestResponse<>(null, s);
        return new ResponseEntity<>(e, HttpStatus.OK);
    }

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<RestResponse<List<ExpenseResponseDTO>>> getExpenses(Principal principal) {
        var all = expenseUseCase.getAll(principal).stream().map(expenseMapper::toResponseDto).toList();
        RestResponse<List<ExpenseResponseDTO>> e = new RestResponse<>(null, all);
        return new ResponseEntity<>(e, HttpStatus.OK);
//        return ResponseEntity.ok(expenseUseCase.getAll(principal));
    }

    @GetMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<RestResponse> getExpense(Principal principal, @PathVariable("id") String id) {
        HttpStatus status = null;
        RestError restError = null;
        ExpenseResponseDTO expenseResponseDTO = null;
        try {
            UUID uuid = UUID.fromString(id);
            var model = expenseUseCase.get(uuid, principal);
            if (model != null) {
                expenseResponseDTO = expenseMapper.toResponseDto(model);
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.NOT_FOUND;
                restError = new RestError("Entity not found", null);
            }
        } catch (IllegalArgumentException e) {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError("Invalid UUID", null);
        }
        RestResponse<ExpenseResponseDTO> e = new RestResponse<>(restError, expenseResponseDTO);
        return new ResponseEntity<>(e, status);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<RestResponse<ExpenseResponseDTO>> addExpense(Principal principal, @RequestBody @Valid ExpenseRequestDTO expenseRequestDTO, BindingResult bindingResult) {
        HttpStatus status = null;
        RestError restError = null;
        ExpenseResponseDTO expenseResponseDTO = null;
        var responseEntity = errorHelper.checkErrors2(bindingResult);
        if (responseEntity == null) {
            status = HttpStatus.OK;
            expenseResponseDTO = expenseMapper.toResponseDto(expenseUseCase.create(userHelper.toModel(principal), expenseMapper.toModel(expenseRequestDTO)));
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, responseEntity);
        }

        RestResponse<ExpenseResponseDTO> e = new RestResponse<>(restError, expenseResponseDTO);
        return new ResponseEntity<>(e, status);
    }

    @PatchMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<RestResponse<ExpenseResponseDTO>> changeExpenseProperty(Principal principal, @PathVariable("id") String id,
                                                                                  @RequestBody @Valid ExpenseRequestDTO expenseRequestDTO, BindingResult bindingResult) {
        HttpStatus status = null;
        RestError restError = null;
        ExpenseResponseDTO expenseResponseDTO = null;
        var responseEntity = errorHelper.checkErrors2(bindingResult);
        if (responseEntity == null) {
            try {
                UUID uuid = UUID.fromString(id);
                var model = expenseUseCase.patch(uuid, userHelper.toModel(principal), expenseMapper.toModel(expenseRequestDTO));
                if (model != null) {
                    expenseResponseDTO = expenseMapper.toResponseDto(model);
                    status = HttpStatus.OK;
                } else {
                    status = HttpStatus.NOT_FOUND;
                    restError = new RestError("Entity not found", null);
                }
            } catch (IllegalArgumentException e) {
                status = HttpStatus.BAD_REQUEST;
                restError = new RestError("Invalid UUID", null);
            }
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, responseEntity);
        }

        RestResponse<ExpenseResponseDTO> e = new RestResponse<>(restError, expenseResponseDTO);
        return new ResponseEntity<>(e, status);
    }

    @PutMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<RestResponse<ExpenseResponseDTO>> changeExpense(Principal principal, @PathVariable("id") String id, @RequestBody @Valid ExpenseRequestDTO expenseRequestDTO,
                                                                          BindingResult bindingResult) {
        HttpStatus status = null;
        RestError restError = null;
        ExpenseResponseDTO expenseResponseDTO = null;
        var responseEntity = errorHelper.checkErrors2(bindingResult);
        if (responseEntity == null) {
            try {
                UUID uuid = UUID.fromString(id);
                var model = expenseUseCase.update(uuid, userHelper.toModel(principal), expenseMapper.toModel(expenseRequestDTO));
                if (model != null) {
                    expenseResponseDTO = expenseMapper.toResponseDto(model);
                    status = HttpStatus.OK;
                } else {
                    status = HttpStatus.NOT_FOUND;
                    restError = new RestError("Entity not found", null);
                }
            } catch (IllegalArgumentException e) {
                status = HttpStatus.BAD_REQUEST;
                restError = new RestError("Invalid UUID", null);
            }
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, responseEntity);
        }
//        if (responseEntity == null) {
//            try {
//                UUID uuid = UUID.fromString(id);
//                responseEntity = ResponseEntity.ok(expenseUseCase.update(uuid, principal, dtoMapper.toModel(expenseRequestDTO)));
//            } catch (IllegalArgumentException e) {
//                responseEntity = new ResponseEntity<>("Invalid UUID", HttpStatus.BAD_REQUEST);
//            } catch (EntityNotFoundException e) {
//                responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
//            } catch (UsernameNotFoundException e) {
//                responseEntity = new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//            }
//        }

        RestResponse<ExpenseResponseDTO> e = new RestResponse<>(restError, expenseResponseDTO);
        return new ResponseEntity<>(e, status);
    }

    @DeleteMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> deleteExpense(Principal principal, @PathVariable("id") String id) {
        ResponseEntity<Object> responseEntity;
        try {
            UUID uuid = UUID.fromString(id);
            expenseUseCase.delete(uuid, principal);
            responseEntity = ResponseEntity.ok(null);
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>("Invalid UUID", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        } catch (UsernameNotFoundException e) {
            responseEntity = new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    private ResponseEntity<Object> check(String id, Principal principal) {
        ResponseEntity<Object> responseEntity;
        try {
            UUID uuid = UUID.fromString(id);
            expenseUseCase.delete(uuid, principal);
            responseEntity = ResponseEntity.ok(null);
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>("Invalid UUID", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        } catch (UsernameNotFoundException e) {
            responseEntity = new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping("sum")
    @TrackExecutionTime
    public double getCategoryRepo(Principal principal) {
        return expenseUseCase.getSum(principal);
    }

    @GetMapping("sum/{groupId}")
    @TrackExecutionTime
    public double getCategoryRepo(Principal principal, @PathVariable("groupId") CategoryEntity categoryEntity) {
        return expenseUseCase.getSum(principal, categoryEntity);
    }
}

