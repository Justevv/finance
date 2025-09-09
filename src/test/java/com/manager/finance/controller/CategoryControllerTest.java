package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.FavoriteCategoryEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.finance.helper.converter.CategoryIdConverter;
import com.manager.finance.helper.prepare.CategoryPrepareHelper;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata.CategorySpringDataRepository;
import com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata.FavoriteCategorySpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import com.manager.user.domain.service.SecurityUserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
class CategoryControllerTest {
    @MockBean
    private UserSpringDataRepository userRepository;
    @MockBean
    private CategorySpringDataRepository categoryRepository;
    @MockBean
    private FavoriteCategorySpringDataRepository favoriteCategorySpringDataRepository;
    @MockBean
    private SecurityUserService securityUserService;
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
        Mockito.when(securityUserService.loadUserByUsername(userEntity.getUsername())).thenReturn(userEntity);
        categoryEntity = categoryPrepareHelper.createCategory();
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getCategories() {
        Mockito.when(categoryRepository.findAll()).thenReturn((List.of(categoryEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/category"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.[0].id").value(categoryEntity.getId().toString()))
                .andExpect(jsonPath("$.payload.[0].name").value(categoryEntity.getName()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getCategory() {
        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/category/{id}", categoryEntity.getId()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.id").value(categoryEntity.getId().toString()))
                .andExpect(jsonPath("$.payload.name").value(categoryEntity.getName()));
    }


    @Test
    @WithMockUser
    @SneakyThrows
    @Disabled
    void putCategory() {
        var newName = "newName";

        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/category/{id}", categoryEntity.getId())
                        .param("name", newName)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(categoryEntity.getId().toString()))
                .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void postCategory() {
        Mockito.when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(favoriteCategorySpringDataRepository.save(Mockito.any(FavoriteCategoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var newName = "newName";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/category")
                        .content("{\"name\":\"newName\"}")
                        .contentType("application/json")
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.id").exists())
                .andExpect(jsonPath("$.payload.name").value(newName));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    @Disabled
    void deleteCategory() {
        Mockito.when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/category/{id}", categoryEntity.getId()))
                .andExpect(status().is(200));
    }

}
