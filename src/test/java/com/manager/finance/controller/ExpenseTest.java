package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.converter.ExpenseIdConverter;
import com.manager.finance.helper.prepare.ExpensePrepareHelper;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.finance.repository.ExpenseRepository;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.service.SecurityUserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({UserPrepareHelper.class, ExpenseIdConverter.class, ExpensePrepareHelper.class})
class ExpenseTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ExpenseRepository expenseRepository;
    @MockBean
    private SecurityUserService securityUserService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    @Autowired
    private ExpensePrepareHelper expensePrepareHelper;
    private UserEntity userEntity;
    private ExpenseEntity expenseEntity;

    @BeforeEach
    void prepare() {
        userEntity = userPrepareHelper.createUser();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        Mockito.when(securityUserService.loadUserByUsername(userEntity.getUsername())).thenReturn(userEntity);
        expenseEntity = expensePrepareHelper.createExpense();
        Mockito.when(expenseRepository.findById(expenseEntity.getId())).thenReturn(Optional.of(expenseEntity));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getExpenses() {
        Mockito.when(expenseRepository.findByUser(userEntity)).thenReturn((List.of(expenseEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/expense")
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].id").value(expenseEntity.getId()))
                .andExpect(jsonPath("[0].description").value(expenseEntity.getDescription()))
                .andExpect(jsonPath("[0].date").isString())
                .andExpect(jsonPath("[0].category.name").value(expenseEntity.getCategory().getName()))
                .andExpect(jsonPath("[0].place.name").value(expenseEntity.getPlace().getName()))
                .andExpect(jsonPath("[0].sum").value(expenseEntity.getSum()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getExpensesPage() {
        Mockito.when(expenseRepository.findByUser(eq(userEntity), any(PageRequest.class))).thenReturn((List.of(expenseEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/expense/page/1")
                        .param("count", "10")
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].id").value(expenseEntity.getId()))
                .andExpect(jsonPath("[0].description").value(expenseEntity.getDescription()))
                .andExpect(jsonPath("[0].date").isString())
                .andExpect(jsonPath("[0].category.name").value(expenseEntity.getCategory().getName()))
                .andExpect(jsonPath("[0].place.name").value(expenseEntity.getPlace().getName()))
                .andExpect(jsonPath("[0].sum").value(expenseEntity.getSum()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getExpense() {
        Mockito.when(expenseRepository.findByUser(eq(userEntity), any(PageRequest.class))).thenReturn((List.of(expenseEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/expense/{id}", expenseEntity.getId())
                        .param("startWith", "0")
                        .param("count", "10")
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(expenseEntity.getId()))
                .andExpect(jsonPath("$.description").value(expenseEntity.getDescription()))
                .andExpect(jsonPath("$.date").isString())
                .andExpect(jsonPath("$.category.name").value(expenseEntity.getCategory().getName()))
                .andExpect(jsonPath("$.place.name").value(expenseEntity.getPlace().getName()))
                .andExpect(jsonPath("$.sum").value(expenseEntity.getSum()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void putExpense() {
        var newDescription = "newDescription";
        var newSum = "400500.0";

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/expense/{id}", expenseEntity.getId())
                        .param("description", newDescription)
                        .param("sum", newSum)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(expenseEntity.getId()))
                .andExpect(jsonPath("$.description").value(newDescription))
                .andExpect(jsonPath("$.date").isString())
                .andExpect(jsonPath("$.category.name").value(expenseEntity.getCategory().getName()))
                .andExpect(jsonPath("$.place.name").value(expenseEntity.getPlace().getName()))
                .andExpect(jsonPath("$.sum").value(newSum));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void postExpense() {
        var newDescription = "newDescription";
        var newSum = "400500.0";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/expense")
                        .content("{\"description\":\"newDescription\",\"sum\":400500.0}")
                        .contentType("application/json")
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description").value(newDescription))
                .andExpect(jsonPath("$.date").isString())
                .andExpect(jsonPath("$.sum").value(newSum));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void deleteExpense() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/expense/{id}", expenseEntity.getId()))
                .andExpect(status().is(200));
    }

}
