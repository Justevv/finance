package com.manager.finance.model;

import com.manager.Manager;
import com.manager.finance.dto.user.UserDTO;
import com.manager.finance.dto.user.UserUpdateDTO;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.prepare.PreparedUser;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.repository.VerificationRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Import({PreparedUser.class})
class UserModelTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private VerificationRepository verificationRepository;
    @MockBean
    private Principal principal;
    @Autowired
    private UserModel userModel;
    @Autowired
    private PreparedUser preparedUser;
    private UserEntity user;

    @BeforeEach
    private void prepareUser() {
        user = preparedUser.createUser();
    }

    @Test
    void getUser_shouldReturnUser_when_userIsExists() {
        var userEntities = List.of(user);
        Mockito.when(userRepository.findAll()).thenReturn(userEntities);

        var userResponseDTO = userModel.getUsers();
        Assertions.assertEquals(user.getEmail(), userResponseDTO.get(0).getEmail());
        Assertions.assertEquals(user.getUsername(), userResponseDTO.get(0).getUsername());
    }

    @Test
    void getUsersAllInfo_shouldReturnUserEntity_when_userIsExists() {
        var userEntities = List.of(user);
        Mockito.when(userRepository.findAll()).thenReturn(userEntities);
        var users = userModel.getUsersAllInfo();
        Assertions.assertEquals(user, users.get(0));
    }

    @Test
    void getUser_shouldReturnUser_when_principalIsExists() {
        var principalName = "principal";
        Mockito.when(principal.getName()).thenReturn(principalName);
        Mockito.when(userRepository.findByUsername(principalName)).thenReturn(Optional.of(user));

        var userResponseDTO = userModel.getUser(principal);
        Assertions.assertEquals(user.getEmail(), userResponseDTO.getEmail());
        Assertions.assertEquals(user.getUsername(), userResponseDTO.getUsername());
    }

    @Test
    @SneakyThrows
    void create_shouldReturnUser_when_userDTOIsOk() {
        var userDTO = new UserDTO();
        userDTO.setPhone("newPhone");
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail");
        userDTO.setPassword("password");
        var user = userModel.create(userDTO);
        Assertions.assertEquals(userDTO.getEmail(), user.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), user.getUsername());
        Assertions.assertEquals(userDTO.getPhone(), user.getPhone());
    }

    @Test
    @SneakyThrows
    void update_shouldReturnUser_when_userDTOIsOk() {
        var userDTO = new UserUpdateDTO();
        userDTO.setPhone("newPhone");
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail");
        userDTO.setPassword("password");
        var userResponseDTO = userModel.update(user, userDTO);
        Assertions.assertEquals(userDTO.getEmail(), userResponseDTO.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), userResponseDTO.getUsername());
        Assertions.assertEquals(userDTO.getPhone(), userResponseDTO.getPhone());
    }

    @Test
    void delete_shouldReturnNull_when_userIsExists() {
        var deleteUser = userModel.delete(user);
        Assertions.assertNull(deleteUser);
    }

}