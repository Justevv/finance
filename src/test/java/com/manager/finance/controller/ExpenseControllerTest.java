package com.manager.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.Manager;
import com.manager.finance.entity.ExpenseEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.prepare.PreparedExpense;
import com.manager.finance.helper.prepare.PreparedUser;
import com.manager.finance.helper.converter.ExpenseIdConverter;
import com.manager.finance.repository.ExpenseRepository;
import com.manager.finance.repository.UserRepository;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({PreparedUser.class, ExpenseIdConverter.class, PreparedExpense.class})
class ExpenseControllerTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ExpenseRepository expenseRepository;
    @Autowired
    private MockMvc mockMvc;
    private UserEntity userEntity;
    private ExpenseEntity expenseEntity;
    @Autowired
    private PreparedUser preparedUser;
    @Autowired
    private PreparedExpense preparedExpense;
    private String token;

    @BeforeEach
    void prepare() {
        userEntity = preparedUser.createUser();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        expenseEntity = preparedExpense.createExpense();
        Mockito.when(expenseRepository.findById(expenseEntity.getId())).thenReturn(Optional.of(expenseEntity));
    }

    @SneakyThrows
    @BeforeEach
    void getToken() {
        var json = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"any\",\"password\":\"1\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, Map.class);
        token = (String) map.get("token");
    }

    @Test
    @SneakyThrows
    void getExpenses() {
        Mockito.when(expenseRepository.findByUser(eq(userEntity), any(PageRequest.class))).thenReturn((List.of(expenseEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/expense")
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("startWith", "0")
                .param("count", "10")
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].id").value(expenseEntity.getId()))
                .andExpect(jsonPath("[0].description").value(expenseEntity.getDescription()))
                .andExpect(jsonPath("[0].date").isString())
                .andExpect(jsonPath("[0].category.name").value(expenseEntity.getCategory().getName()))
                .andExpect(jsonPath("[0].place.name").value(expenseEntity.getPlace().getName()))
                .andExpect(jsonPath("[0].sum").value(expenseEntity.getSum()))
                .andExpect(jsonPath("[0].user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void getExpense() {
        Mockito.when(expenseRepository.findByUser(eq(userEntity), any(PageRequest.class))).thenReturn((List.of(expenseEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/expense/{id}", expenseEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("startWith", "0")
                .param("count", "10")
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(expenseEntity.getId()))
                .andExpect(jsonPath("$.description").value(expenseEntity.getDescription()))
                .andExpect(jsonPath("$.date").isString())
                .andExpect(jsonPath("$.category.name").value(expenseEntity.getCategory().getName()))
                .andExpect(jsonPath("$.place.name").value(expenseEntity.getPlace().getName()))
                .andExpect(jsonPath("$.sum").value(expenseEntity.getSum()))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void putExpense() {
        var newDescription = "newDescription";
        var newSum = "400500.0";

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/expense/{id}", expenseEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("description", newDescription)
                .param("sum", newSum)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(expenseEntity.getId()))
                .andExpect(jsonPath("$.description").value(newDescription))
                .andExpect(jsonPath("$.date").isString())
                .andExpect(jsonPath("$.category.name").value(expenseEntity.getCategory().getName()))
                .andExpect(jsonPath("$.place.name").value(expenseEntity.getPlace().getName()))
                .andExpect(jsonPath("$.sum").value(newSum))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void postExpense() {
        var newDescription = "newDescription";
        var newSum = "400500.0";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/expense")
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("description", newDescription)
                .param("sum", newSum)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description").value(newDescription))
                .andExpect(jsonPath("$.date").isString())
                .andExpect(jsonPath("$.sum").value(newSum))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void deleteExpense() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/expense/{id}", expenseEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
        )
                .andExpect(status().is(200));
    }

}
