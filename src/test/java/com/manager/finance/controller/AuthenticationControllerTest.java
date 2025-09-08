package com.manager.finance.controller;


import com.manager.Manager;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.AuthenticationLogRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.UserRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({UserPrepareHelper.class})
@ActiveProfiles("test")
class AuthenticationControllerTest {
    private static final String LOGIN_API = "/v1/auth/login";
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationLogRepository authenticationLogRepository;
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
    @SneakyThrows
    void authenticate_shouldReturnAuthenticationAndOk_when_userIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_API)
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
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"user","password":"1"}"""))
                .andExpect(status().is(401))
                .andExpect(content().contentType("application/json"));
    }

}