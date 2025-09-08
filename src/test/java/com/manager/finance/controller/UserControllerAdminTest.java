package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.user.entity.PermissionEntity;
import com.manager.user.entity.RoleEntity;
import com.manager.user.entity.UserEntity;
import com.manager.finance.helper.converter.RoleIdConverter;
import com.manager.finance.helper.converter.UserIdConverter;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.repository.PhoneVerificationRepository;
import com.manager.user.repository.RoleRepository;
import com.manager.user.repository.UserRepository;
import com.manager.user.repository.EmailVerificationRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({UserPrepareHelper.class, UserIdConverter.class, RoleIdConverter.class})
@ActiveProfiles("test")
class UserControllerAdminTest {
    private static final String USER_WITH_ID_API = "/v1/admin/user/{id}";
    private static final String USER_API = "/v1/admin/user";
    private static final String USERNAME_PARAMETER = "username";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String PHONE_PARAMETER = "phone";
    private static final String EMAIL_PARAMETER = "email";
    private static final String ROLES_PARAMETER = "roles";
    private static final String NEW_USERNAME = "new";
    private static final String NEW_PHONE = "1";
    private static final String NEW_EMAIL = "st@a.ru";
    private static final String NEW_ROLE = "newRole";
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmailVerificationRepository emailVerificationRepository;
    @MockBean
    private PhoneVerificationRepository phoneVerificationRepository;
    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    private UserEntity user;
    private RoleEntity role;

    @BeforeEach
    private void prepare() {
        user = userPrepareHelper.createUser();
        role = user.getRoles().iterator().next();
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(authorities = {"user:read"})
    @SneakyThrows
    void getUsers_shouldReturnUserEntitiesAndOk_when_userIsExists() {
        Mockito.when(userRepository.findAll()).thenReturn((List.of(user)));
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].username").value(user.getUsername()))
                .andExpect(jsonPath("[0].email").value(user.getEmail()))
                .andExpect(jsonPath("[0].phone").value(user.getPhone()))
                .andExpect(jsonPath("[0].phoneConfirmed").value(user.isPhoneConfirmed()))
                .andExpect(jsonPath("[0].emailConfirmed").value(user.isEmailConfirmed()))
                .andExpect(jsonPath("[0].roles.[0].name").value(role.getName()))
                .andExpect(jsonPath("[0].roles.[0].permissions").value(PermissionEntity.ALL_READ.toString()));
    }

    @Test
    @WithMockUser(authorities = {"user:read"})
    @SneakyThrows
    void getUser_shouldReturnUserEntityAndOk_when_userIsExists() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn((Optional.of(user)));
        mockMvc.perform(MockMvcRequestBuilders.get(USER_WITH_ID_API, user.getId()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()))
                .andExpect(jsonPath("$.phoneConfirmed").value(user.isPhoneConfirmed()))
                .andExpect(jsonPath("$.emailConfirmed").value(user.isEmailConfirmed()))
                .andExpect(jsonPath("$.roles.[0].name").value(role.getName()))
                .andExpect(jsonPath("$.roles.[0].permissions").value(PermissionEntity.ALL_READ.toString()));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void updateUser_shouldReturnUserAndOk_when_userIsExists() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.ofNullable(role));
        role.setName(NEW_ROLE);
        mockMvc.perform(MockMvcRequestBuilders.put(USER_WITH_ID_API, user.getId())
                        .param(USERNAME_PARAMETER, NEW_USERNAME)
                        .param(PASSWORD_PARAMETER, "1")
                        .param(PHONE_PARAMETER, NEW_PHONE)
                        .param(EMAIL_PARAMETER, NEW_EMAIL)
                        .param("emailConfirmed", String.valueOf(true))
                        .param("phoneConfirmed", String.valueOf(true))
                        .param(ROLES_PARAMETER, String.valueOf(role.getId()))
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.email").value(NEW_EMAIL))
                .andExpect(jsonPath("$.emailConfirmed").value(true))
                .andExpect(jsonPath("$.phoneConfirmed").value(true))
                .andExpect(jsonPath("$.roles.[0].name").value(NEW_ROLE));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void updateUser_shouldReturnException_when_userHasAuthoritiesAndEmailIsWrong() {
        var newEmail = "wrongEmail";

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        mockMvc.perform(MockMvcRequestBuilders.put(USER_WITH_ID_API, user.getId())
                        .param(EMAIL_PARAMETER, newEmail)
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void createUser_shouldReturnException_when_userIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                        .param(USERNAME_PARAMETER, "user")
                        .param(PASSWORD_PARAMETER, "1")
                        .param(PHONE_PARAMETER, NEW_PHONE)
                        .param(EMAIL_PARAMETER, NEW_EMAIL)
                )
                .andExpect(status().is(409));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void createUser_shouldReturnException_when_usernameIsNull() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void createUser_shouldReturnUsersAndOk_when_userIsNotExists() {
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.ofNullable(role));
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                        .param(USERNAME_PARAMETER, NEW_USERNAME)
                        .param(PASSWORD_PARAMETER, "1")
                        .param(PHONE_PARAMETER, NEW_PHONE)
                        .param(EMAIL_PARAMETER, NEW_EMAIL)
                        .param(ROLES_PARAMETER, String.valueOf(role.getId()))
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.email").value(NEW_EMAIL))
                .andExpect(jsonPath("$.roles.[0].name").value(role.getName()))
                .andExpect(jsonPath("$.roles.[0].permissions").value(PermissionEntity.ALL_READ.toString()));
    }

    @Test
    @WithMockUser(authorities = {"user:delete"})
    @SneakyThrows
    void deleteUser_shouldReturnNullAndOk_when_userIsExists() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_WITH_ID_API, user.getId()))
                .andExpect(status().is(200));
    }

}