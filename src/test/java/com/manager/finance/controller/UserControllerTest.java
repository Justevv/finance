package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.prepare.PreparedUser;
import com.manager.finance.helper.converter.UserIdConverter;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.repository.VerificationRepository;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({PreparedUser.class, UserIdConverter.class})
class UserControllerTest {
    private static final String USER_HIMSELF_API = "/v1/user/me";
    private static final String USER_WITH_ID_API = "/v1/user/{id}";
    private static final String USER_API = "/v1/user";
    private static final String USERNAME_PARAMETER = "username";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String PHONE_PARAMETER = "phone";
    private static final String EMAIL_PARAMETER = "email";
    private static final String NEW_USERNAME = "new";
    private static final String NEW_PHONE = "1";
    private static final String NEW_EMAIL = "st@a.ru";
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private VerificationRepository verificationRepository;
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
    private void prepare() {
        userEntity = preparedUser.createUser();
        var principalName = "principal";
        Mockito.when(principal.getName()).thenReturn(principalName);
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        Mockito.when(userService.loadUserByUsername(userEntity.getUsername())).thenReturn(userEntity);
    }

    @Test
    @SneakyThrows
    void authenticate_shouldReturnAuthenticationAndOk_when_userIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"user","password":"password"}"""))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @SneakyThrows
    void authenticate_shouldReturnUnauthorized_when_userIsNotExists() {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"user","password":"1"}"""))
                .andExpect(status().is(401))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getUser_shouldReturnUserAndOk_when_userIsExists() {
        Mockito.when(userRepository.findAll()).thenReturn((List.of(userEntity)));
        System.out.println(userRepository.findAll());
        mockMvc.perform(MockMvcRequestBuilders.get(USER_HIMSELF_API))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.email").value(userEntity.getEmail()))
                .andExpect(jsonPath("$.phone").value(userEntity.getPhone()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void putUser_shouldReturnUserAndOk_when_userIsExists() {
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.put(USER_HIMSELF_API)
                        .param(USERNAME_PARAMETER, NEW_USERNAME)
                        .param(PASSWORD_PARAMETER, "1")
                        .param(PHONE_PARAMETER, NEW_PHONE)
                        .param(EMAIL_PARAMETER, NEW_EMAIL)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.email").value(NEW_EMAIL));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void deleteUser_shouldReturnNullAndOk_when_userIsExists() {
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_HIMSELF_API))
                .andExpect(status().is(200));
    }


    @Test
    @WithMockUser(authorities = {"user:read"})
    @SneakyThrows
    void getUsers_shouldReturnUsersAndOk_when_userHasAuthorities() {
        Mockito.when(userRepository.findAll()).thenReturn((List.of(userEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].username").value(userEntity.getUsername()))
                .andExpect(jsonPath("[0].email").value(userEntity.getEmail()))
                .andExpect(jsonPath("[0].phone").value(userEntity.getPhone()));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void putUser_shouldReturnUserAndOk_when_userHasAuthorities() {
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.put(USER_WITH_ID_API, userEntity.getId())
                        .param(USERNAME_PARAMETER, NEW_USERNAME)
                        .param(PASSWORD_PARAMETER, "1")
                        .param(PHONE_PARAMETER, NEW_PHONE)
                        .param(EMAIL_PARAMETER, NEW_EMAIL)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.email").value(NEW_EMAIL));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void putUser_shouldReturnException_when_userIsExistsAndEmailIsWrong() {
        var newEmail = "wrongEmail";

        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.put(USER_HIMSELF_API)
                        .param(EMAIL_PARAMETER, newEmail)
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void putUser_shouldReturnException_when_userHasAuthoritiesAndEmailIsWrong() {
        var newEmail = "wrongEmail";

        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.put(USER_WITH_ID_API, userEntity.getId())
                        .param(EMAIL_PARAMETER, newEmail)
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @SneakyThrows
    void postUser_shouldReturnException_when_userIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                        .param(USERNAME_PARAMETER, "user")
                        .param(PASSWORD_PARAMETER, "1")
                        .param(PHONE_PARAMETER, NEW_PHONE)
                        .param(EMAIL_PARAMETER, NEW_EMAIL)
                )
                .andExpect(status().is(409));
    }

    @Test
    @SneakyThrows
    void postUser_shouldReturnException_when_usernameIsNull() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @SneakyThrows
    void postUser_shouldReturnUsersAndOk_when_userIsNotExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                        .param(USERNAME_PARAMETER, NEW_USERNAME)
                        .param(PASSWORD_PARAMETER, "1")
                        .param(PHONE_PARAMETER, NEW_PHONE)
                        .param(EMAIL_PARAMETER, NEW_EMAIL)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.email").value(NEW_EMAIL));
    }

    @Test
    @WithMockUser(authorities = {"user:delete"})
    @SneakyThrows
    void deleteUser_shouldReturnNullAndOk_when_userHasAuthorities() {
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_WITH_ID_API, userEntity.getId()))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(authorities = {"user:read"})
    @SneakyThrows
    void getUsers_shouldReturnUserEntitiesAndOk_when_userHasAuthorities() {
        Mockito.when(userRepository.findAll()).thenReturn((List.of(userEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API + "/all"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].username").value(userEntity.getUsername()))
                .andExpect(jsonPath("[0].email").value(userEntity.getEmail()))
                .andExpect(jsonPath("[0].phone").value(userEntity.getPhone()))
                .andExpect(jsonPath("[0].phoneConfirmed").value(userEntity.isPhoneConfirmed()))
                .andExpect(jsonPath("[0].emailConfirmed").value(userEntity.isEmailConfirmed()));
    }

}
