package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.prepare.PreparedUser;
import com.manager.finance.helper.converter.UserIdConverter;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({PreparedUser.class, UserIdConverter.class})
class UserControllerTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private Principal principal;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PreparedUser preparedUser;
    private UserEntity userEntity;

    @BeforeEach
    private void prepareUser() {
        userEntity = preparedUser.createUser();
        var principalName = "principal";
        Mockito.when(principal.getName()).thenReturn(principalName);
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        Mockito.when(userService.loadUserByUsername(userEntity.getUsername())).thenReturn(userEntity);
    }


    @Test
    @WithMockUser
    @SneakyThrows
    void getUsers() {
        Mockito.when(userRepository.findAll()).thenReturn((List.of(userEntity)));
        System.out.println(userRepository.findAll());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/user/me"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.email").value(userEntity.getEmail()))
                .andExpect(jsonPath("$.phone").value(userEntity.getPhone()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void putUser() {
        var newUsername = "new";
        var newPhone = "32245";
        var newEmail = "st@a.ru";
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/user/me")
                .param("username", newUsername)
                .param("password", "1")
                .param("phone", newPhone)
                .param("email", newEmail)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(newUsername))
                .andExpect(jsonPath("$.phone").value(newPhone))
                .andExpect(jsonPath("$.email").value(newEmail));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void deleteUser() {
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/user/me"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void authenticate_callApiLogin_shouldReturnDataAndOk_when_userIsAdmin() {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @WithMockUser(authorities={"user:read"})
    @SneakyThrows
    void getUsersSuperAdmin() {
        Mockito.when(userRepository.findAll()).thenReturn((List.of(userEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/user/all"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].username").value(userEntity.getUsername()))
                .andExpect(jsonPath("[0].email").value(userEntity.getEmail()))
                .andExpect(jsonPath("[0].phone").value(userEntity.getPhone()))
                .andExpect(jsonPath("[0].phoneConfirmed").value(userEntity.isPhoneConfirmed()))
                .andExpect(jsonPath("[0].emailConfirmed").value(userEntity.isEmailConfirmed()));
    }

    @Test
    @WithMockUser(authorities={"user:read"})
    @SneakyThrows
    void getUsersAdmin() {
        Mockito.when(userRepository.findAll()).thenReturn((List.of(userEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/user"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].username").value(userEntity.getUsername()))
                .andExpect(jsonPath("[0].email").value(userEntity.getEmail()))
                .andExpect(jsonPath("[0].phone").value(userEntity.getPhone()));
    }

    @Test
    @WithMockUser(authorities={"user:write"})
    @SneakyThrows
    void putUserAdmin() {
        var newUsername = "new";
        var newPhone = "32245";
        var newEmail = "st@a.ru";

        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/user/{id}", userEntity.getId())
                        .param("username", newUsername)
                        .param("password", "1")
                        .param("phone", newPhone)
                        .param("email", newEmail)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(newUsername))
                .andExpect(jsonPath("$.phone").value(newPhone))
                .andExpect(jsonPath("$.email").value(newEmail));
    }

    @Test
    @SneakyThrows
    void postUser() {
        var newUsername = "new";
        var newPhone = "32245";
        var newEmail = "st@a.ru";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/user")
                        .param("username", newUsername)
                        .param("password", "1")
                        .param("phone", newPhone)
                        .param("email", newEmail)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(newUsername))
                .andExpect(jsonPath("$.phone").value(newPhone))
                .andExpect(jsonPath("$.email").value(newEmail));
    }

    @Test
    @WithMockUser(authorities={"user:delete"})
    @SneakyThrows
    void deleteUserAdmin() {
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/user/{id}", userEntity.getId()))
                .andExpect(status().is(200));
    }


}
