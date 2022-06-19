package com.manager.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.Manager;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.PreparedUser;
import com.manager.finance.helper.UserIdConverter;
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
@Import({PreparedUser.class, UserIdConverter.class})
class UserControllerSuperAdminTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    private UserEntity userEntity;
    @Autowired
    private PreparedUser preparedUser;
    private String token;

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

    @BeforeEach
    private void createSuperAdmin() {
        userEntity = preparedUser.createSuperAdmin();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
    }

    @Test
    @SneakyThrows
    void authenticate_callApiLogin_shouldReturnDataAndOk_when_userIsAdmin() {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"any\",\"password\":\"1\"}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @SneakyThrows
    void getUsers() {
        Mockito.when(userRepository.findAll()).thenReturn((List.of(userEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/user/all")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].username").value(userEntity.getUsername()))
                .andExpect(jsonPath("[0].email").value(userEntity.getEmail()))
                .andExpect(jsonPath("[0].phone").value(userEntity.getPhone()))
                .andExpect(jsonPath("[0].phoneConfirmed").value(userEntity.isPhoneConfirmed()))
                .andExpect(jsonPath("[0].emailConfirmed").value(userEntity.isEmailConfirmed()));
    }

}
