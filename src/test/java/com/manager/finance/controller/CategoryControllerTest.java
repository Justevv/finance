package com.manager.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.Manager;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.prepare.PreparedCategory;
import com.manager.finance.helper.converter.CategoryIdConverter;
import com.manager.finance.helper.prepare.PreparedUser;
import com.manager.finance.repository.CategoryRepository;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({PreparedUser.class, PreparedCategory.class, CategoryIdConverter.class})
class CategoryControllerTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    private MockMvc mockMvc;
    private UserEntity userEntity;
    private CategoryEntity categoryEntity;
    @Autowired
    private PreparedUser preparedUser;
    @Autowired
    private PreparedCategory preparedCategory;
    private String token;

    @BeforeEach
    void prepare() {
        userEntity = preparedUser.createUser();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        categoryEntity = preparedCategory.createCategory();
        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
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
    void getCategories() {
        Mockito.when(categoryRepository.findByUser(userEntity)).thenReturn((List.of(categoryEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/category")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].id").value(categoryEntity.getId()))
                .andExpect(jsonPath("[0].name").value(categoryEntity.getName()))
                .andExpect(jsonPath("[0].user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void getCategory() {
        Mockito.when(categoryRepository.findByUser(userEntity)).thenReturn((List.of(categoryEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/category/{id}", categoryEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(categoryEntity.getId()))
                .andExpect(jsonPath("$.name").value(categoryEntity.getName()))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }


    @Test
    @SneakyThrows
    void putCategory() {
        var newName = "newName";

        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/category/{id}", categoryEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("name", newName)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(categoryEntity.getId()))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void postCategory() {
        var newName = "newName";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/category")
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("name", newName)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void deleteCategory() {
        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/category/{id}", categoryEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
        )
                .andExpect(status().is(200));
    }

}
