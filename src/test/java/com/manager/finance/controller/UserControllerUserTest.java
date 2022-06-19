package com.manager.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.Manager;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.prepare.PreparedUser;
import com.manager.finance.helper.converter.UserIdConverter;
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

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({PreparedUser.class, UserIdConverter.class})
class UserControllerUserTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private Principal principal;
    @Autowired
    private MockMvc mockMvc;
    private UserEntity userEntity;
    @Autowired
    private PreparedUser preparedUser;
    private String token;

    @BeforeEach
    private void prepareUser() {
        userEntity = preparedUser.createUser();
        var principalName = "principal";
        Mockito.when(principal.getName()).thenReturn(principalName);
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
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
    void login() {
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
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
        var userEntity = preparedUser.createAdmin();
        Mockito.when(userRepository.findAll()).thenReturn((List.of(userEntity)));
        System.out.println(userRepository.findAll());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/user/me")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.email").value(userEntity.getEmail()))
                .andExpect(jsonPath("$.phone").value(userEntity.getPhone()));
    }

    @Test
    @SneakyThrows
    void putUser() {
        var newUsername = "new";
        var newPhone = "32245";
        var newEmail = "st@a.ru";
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/user/me")
                .header(HttpHeaders.AUTHORIZATION, token)
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
    void deleteUser() {
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/user/me")
                .header(HttpHeaders.AUTHORIZATION, token)
        )
                .andExpect(status().is(200));
    }

}
