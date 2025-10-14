package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.finance.helper.converter.RoleIdConverter;
import com.manager.finance.helper.converter.UserIdConverter;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.PhoneVerificationSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.RoleRepository;
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
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
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
    private UserSpringDataRepository userRepository;
    @MockBean
    private EmailVerificationSpringDataRepository emailVerificationRepository;
    @MockBean
    private PhoneVerificationSpringDataRepository phoneVerificationRepository;
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
                .andExpect(jsonPath("$.payload.[0].username").value(user.getUsername()))
                .andExpect(jsonPath("$.payload.[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.payload.[0].phone").value(user.getPhone()))
                .andExpect(jsonPath("$.payload.[0].isPhoneConfirmed").value(user.isPhoneConfirmed()))
                .andExpect(jsonPath("$.payload.[0].isEmailConfirmed").value(user.isEmailConfirmed()))
                .andExpect(jsonPath("$.payload.[0].roles.[0].name").value(role.getName()))
                .andExpect(jsonPath("$.payload.[0].roles.[0].permissions").value(PermissionEntity.ALL_READ.toString()));
    }

    @Test
    @WithMockUser(authorities = {"user:read"})
    @SneakyThrows
    void getUser_shouldReturnUserEntityAndOk_when_userIsExists() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn((Optional.of(user)));
        mockMvc.perform(MockMvcRequestBuilders.get(USER_WITH_ID_API, user.getId()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.username").value(user.getUsername()))
                .andExpect(jsonPath("$.payload.email").value(user.getEmail()))
                .andExpect(jsonPath("$.payload.phone").value(user.getPhone()))
                .andExpect(jsonPath("$.payload.isPhoneConfirmed").value(user.isPhoneConfirmed()))
                .andExpect(jsonPath("$.payload.isEmailConfirmed").value(user.isEmailConfirmed()))
                .andExpect(jsonPath("$.payload.roles.[0].name").value(role.getName()))
                .andExpect(jsonPath("$.payload.roles.[0].permissions").value(PermissionEntity.ALL_READ.toString()));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void updateUser_shouldReturnUserAndOk_when_userIsExists() {
        UserEntity userEntity = UserEntity.builder()
                .id(user.getId())
                .username("new")
                .email("st@a.ru")
                .phone("1")
                .password("$2a$04$kLf5hQQ8yshxEfcMk9etVupP2It5u889YM9KLVpuAnSEAvc3oDq.6")
                .isEmailConfirmed(true)
                .isPhoneConfirmed(true)
                .roles(user.getRoles())
                .build();
        Mockito.when(userRepository.save(any())).thenReturn(userEntity);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.ofNullable(role));
        role.setName(NEW_ROLE);
        mockMvc.perform(MockMvcRequestBuilders.put(USER_WITH_ID_API, user.getId())
                        .content("""
                                {
                                    "user":{
                                        "username": "new",
                                        "email": "st@a.ru",
                                        "phone": "1",
                                        "password":"1"
                                    },
                                    "isPhoneConfirmed":false,
                                    "isEmailConfirmed":false
                                }""")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.payload.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.payload.email").value(NEW_EMAIL))
                .andExpect(jsonPath("$.payload.isEmailConfirmed").value(true))
                .andExpect(jsonPath("$.payload.isPhoneConfirmed").value(true));
//                .andExpect(jsonPath("$.payload.roles.[0].name").value(NEW_ROLE));
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
                .andExpect(content().contentType("application/problem+json"));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void createUser_shouldReturnException_when_userIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                        .content("""
                                {
                                    "user":{
                                        "username": "user",
                                        "email": "st@a.ru",
                                        "phone": "1",
                                        "password":"1"
                                    },
                                    "isPhoneConfirmed":false,
                                    "isEmailConfirmed":false
                                }""")
                        .contentType(MediaType.APPLICATION_JSON)
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
                .andExpect(content().contentType("application/problem+json"));
    }

    @Test
    @WithMockUser(authorities = {"user:write"})
    @SneakyThrows
    void createUser_shouldReturnUsersAndOk_when_userIsNotExists() {
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.ofNullable(role));
        Mockito.when(roleRepository.findByName(role.getName())).thenReturn(Optional.ofNullable(role));
        mockMvc.perform(MockMvcRequestBuilders.post(USER_API)
                        .content("""
                                {
                                    "user":{
                                        "username": "new",
                                        "email": "st@a.ru",
                                        "phone": "1",
                                        "password":"1"
                                    },
                                    "isPhoneConfirmed":false,
                                    "isEmailConfirmed":false
                                }""")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.username").value(NEW_USERNAME))
                .andExpect(jsonPath("$.payload.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.payload.email").value(NEW_EMAIL))
                .andExpect(jsonPath("$.payload.roles.[0].name").value(role.getName()))
                .andExpect(jsonPath("$.payload.roles.[0].permissions").value(PermissionEntity.ALL_READ.toString()));
    }

    @Test
    @WithMockUser(authorities = {"user:delete"})
    @SneakyThrows
    void deleteUser_shouldReturnNullAndOk_when_userIsExists() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_WITH_ID_API, user.getId()))
                .andExpect(status().is(204));
    }

}