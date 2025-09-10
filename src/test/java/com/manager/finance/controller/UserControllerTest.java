package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.helper.WithMockCustomUser;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.ExpenseEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.finance.helper.converter.UserIdConverter;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.PhoneVerificationSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.EmailVerificationSpringDataRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({UserPrepareHelper.class, UserIdConverter.class})
@ActiveProfiles("test")
class UserControllerTest {
    private static final String USER_HIMSELF_API = "/v1/user";
    private static final String USER_API = "/v1/user";
    private static final String USERNAME_PARAMETER = "username";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String PHONE_PARAMETER = "phone";
    private static final String EMAIL_PARAMETER = "email";
    private static final String NEW_USERNAME = "new";
    private static final String NEW_PHONE = "1";
    private static final String NEW_EMAIL = "st@a.ru";
    @MockBean
    private UserSpringDataRepository userRepository;
    @MockBean
    private EmailVerificationSpringDataRepository emailVerificationRepository;
    @MockBean
    private PhoneVerificationSpringDataRepository phoneVerificationRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    private UserEntity userEntity;

    @BeforeEach
    private void prepare() {
        userEntity = userPrepareHelper.createUser();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
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
    @WithMockCustomUser
    @SneakyThrows
    void putUser_shouldReturnUserAndOk_when_userIsExists() {
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        var content = String.format("""
                                {
                                    "username": "%s",
                                    "email": "%s",
                                    "phone": "%s",
                                    "password":"1"
                                }""", NEW_USERNAME, NEW_EMAIL, NEW_PHONE);
        mockMvc.perform(MockMvcRequestBuilders.put(USER_HIMSELF_API)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.payload.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.payload.email").value(NEW_EMAIL));
    }

    @Test
    @WithMockCustomUser
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
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.email").value(userEntity.getEmail()))
                .andExpect(jsonPath("$.phone").value(userEntity.getPhone()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void putUser_shouldReturnException_when_userIsExistsAndEmailIsWrong() {
        var newEmail = "wrongEmail";

        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.put(USER_HIMSELF_API)
                                                .content("""
                                {
                                    "email": "wrongEmail"
                                }""")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @SneakyThrows
    void postUser_shouldReturnException_when_userIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                        .content("""
                                {
                                    "username": "user",
                                    "email": "st@a.ru",
                                    "phone": "1",
                                    "password":"1"
                                }""")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(409));
    }

    @Test
    @SneakyThrows
    void postUser_shouldReturnException_when_usernameIsNull() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @SneakyThrows
    void postUser_shouldReturnUsersAndOk_when_userIsNotExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                        .content("""
                                {
                                    "username": "new",
                                    "email": "st@a.ru",
                                    "phone": "1",
                                    "password":"1"
                                }""")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.payload.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.payload.email").value(NEW_EMAIL));
    }

}
