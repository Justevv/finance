package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.converter.PlaceIdConverter;
import com.manager.finance.helper.prepare.PlacePrepareHelper;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.finance.repository.PlaceRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({UserPrepareHelper.class, PlacePrepareHelper.class, PlaceIdConverter.class})
class PlaceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PlaceRepository placeRepository;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    @Autowired
    private PlacePrepareHelper placePrepareHelper;
    private UserEntity userEntity;
    private PlaceEntity placeEntity;

    @BeforeEach
    void prepare() {
        userEntity = userPrepareHelper.createUser();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        Mockito.when(userService.loadUserByUsername(userEntity.getUsername())).thenReturn(userEntity);
        placeEntity = placePrepareHelper.createPlace();
        Mockito.when(placeRepository.findById(placeEntity.getId())).thenReturn(Optional.of(placeEntity));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getPlaces() {
        Mockito.when(placeRepository.findByUser(userEntity)).thenReturn((List.of(placeEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/place"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].id").value(placeEntity.getId()))
                .andExpect(jsonPath("[0].name").value(placeEntity.getName()))
                .andExpect(jsonPath("[0].address").value(placeEntity.getAddress()))
                .andExpect(jsonPath("[0].user.username").value(userEntity.getUsername()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void getPlace() {
        Mockito.when(placeRepository.findByUser(userEntity)).thenReturn((List.of(placeEntity)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/place/{id}", placeEntity.getId()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(placeEntity.getId()))
                .andExpect(jsonPath("$.name").value(placeEntity.getName()))
                .andExpect(jsonPath("$.address").value(placeEntity.getAddress()))
                .andExpect(jsonPath("$.user.username").value(userEntity.getUsername()));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void putPlace() {
        var newName = "newName";
        var newAddress = "newAddress";

        Mockito.when(placeRepository.findById(placeEntity.getId())).thenReturn(Optional.of(placeEntity));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/place/{id}", placeEntity.getId())
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
    @WithMockUser
    @SneakyThrows
    void postPlace() {
        var newName = "newName";
        var newAddress = "newAddress";
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/place")
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
    @WithMockUser
    @SneakyThrows
    void deletePlace() {
        Mockito.when(placeRepository.findById(placeEntity.getId())).thenReturn(Optional.of(placeEntity));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/place/{id}", placeEntity.getId()))
                .andExpect(status().is(200));
    }

}
