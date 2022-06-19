package com.manager.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.Manager;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.converter.PlaceIdConverter;
import com.manager.finance.helper.prepare.PreparedPlace;
import com.manager.finance.helper.prepare.PreparedUser;
import com.manager.finance.repository.PlaceRepository;
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
@Import({PreparedUser.class, PreparedPlace.class, PlaceIdConverter.class})
class PlaceControllerTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PlaceRepository placeRepository;
    @Autowired
    private MockMvc mockMvc;
    private UserEntity userEntity;
    private PlaceEntity placeEntity;
    @Autowired
    private PreparedUser preparedUser;
    @Autowired
    private PreparedPlace preparedPlace;
    private String token;

    @BeforeEach
    void prepare() {
        userEntity = preparedUser.createUser();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        placeEntity = preparedPlace.createPlace();
        Mockito.when(placeRepository.findById(placeEntity.getId())).thenReturn(Optional.of(placeEntity));
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
    void getPlaces() {
        Mockito.when(placeRepository.findByUser(userEntity)).thenReturn((List.of(placeEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/place")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].id").value(placeEntity.getId()))
                .andExpect(jsonPath("[0].name").value(placeEntity.getName()))
                .andExpect(jsonPath("[0].address").value(placeEntity.getAddress()))
                .andExpect(jsonPath("[0].user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void getPlace() {
        Mockito.when(placeRepository.findByUser(userEntity)).thenReturn((List.of(placeEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/place/{id}", placeEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(placeEntity.getId()))
                .andExpect(jsonPath("$.name").value(placeEntity.getName()))
                .andExpect(jsonPath("$.address").value(placeEntity.getAddress()))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void putPlace() {
        var newName = "newName";
        var newAddress = "newAddress";

        Mockito.when(placeRepository.findById(placeEntity.getId())).thenReturn(Optional.of(placeEntity));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/place/{id}", placeEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("name", newName)
                .param("address", newAddress)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(placeEntity.getId()))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.address").value(newAddress))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void postPlace() {
        var newName = "newName";
        var newAddress = "newAddress";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/place")
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("name", newName)
                .param("address", newAddress)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.address").value(newAddress))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @SneakyThrows
    void deletePlace() {
        Mockito.when(placeRepository.findById(placeEntity.getId())).thenReturn(Optional.of(placeEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/place/{id}", placeEntity.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
        )
                .andExpect(status().is(200));
    }

}
