package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.converter.CategoryIdConverter;
import com.manager.finance.helper.prepare.CategoryPrepareHelper;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.finance.repository.CategoryRepository;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({UserPrepareHelper.class, CategoryPrepareHelper.class, CategoryIdConverter.class})
class CategoryTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    @Autowired
    private CategoryPrepareHelper categoryPrepareHelper;
    private UserEntity userEntity;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void prepare() {
        userEntity = userPrepareHelper.createUser();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        Mockito.when(userService.loadUserByUsername(userEntity.getUsername())).thenReturn(userEntity);
        categoryEntity = categoryPrepareHelper.createCategory();
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getCategories() {
        Mockito.when(categoryRepository.findByUser(userEntity)).thenReturn((List.of(categoryEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/category"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].id").value(categoryEntity.getId()))
                .andExpect(jsonPath("[0].name").value(categoryEntity.getName()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getCategory() {
        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/category/{id}", categoryEntity.getId()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(categoryEntity.getId()))
                .andExpect(jsonPath("$.name").value(categoryEntity.getName()));
    }


    @Test
    @WithMockUser
    @SneakyThrows
    void putCategory() {
        var newName = "newName";

        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/category/{id}", categoryEntity.getId())
                        .param("name", newName)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(categoryEntity.getId()))
                .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void postCategory() {
        var newName = "newName";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/category")
                        .param("name", newName)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void deleteCategory() {
        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/category/{id}", categoryEntity.getId()))
                .andExpect(status().is(200));
    }

}
