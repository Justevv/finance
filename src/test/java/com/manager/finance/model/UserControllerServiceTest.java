package com.manager.finance.model;

import com.manager.user.dto.UserDTO;
import com.manager.user.dto.UserUpdateDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.PhoneVerificationRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.UserRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.EmailVerificationRepository;
import com.manager.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.security.Principal;
import java.util.Optional;

@SpringBootTest
@Import({UserPrepareHelper.class})
class UserControllerServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmailVerificationRepository emailVerificationRepository;
    @MockBean
    private PhoneVerificationRepository phoneVerificationRepository;
    @MockBean
    private Principal principal;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    private UserEntity user;

    @BeforeEach
    private void prepareUser() {
        var principalName = "user";
        Mockito.when(principal.getName()).thenReturn(principalName);
        user = userPrepareHelper.createUser();
        Mockito.when(userRepository.findByUsername(principalName)).thenReturn(Optional.of(user));
    }

    @Test
    void getUser_shouldReturnUser_when_principalIsExists() {
        var userResponseDTO = userService.getUser(principal);
        Assertions.assertEquals(user.getEmail(), userResponseDTO.getEmail());
        Assertions.assertEquals(user.getUsername(), userResponseDTO.getUsername());
    }

    @Test
    void create_shouldReturnUser_when_userDTOIsOk() {
        var userDTO = new UserDTO();
        userDTO.setPhone("newPhone");
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail");
        userDTO.setPassword("password");
        var user = userService.createAndGetDTO(userDTO);
        Assertions.assertEquals(userDTO.getEmail(), user.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), user.getUsername());
        Assertions.assertEquals(userDTO.getPhone(), user.getPhone());
    }

    @Test
    void update_shouldReturnUser_when_userDTOIsOk() {
        var userDTO = new UserUpdateDTO();
        userDTO.setPhone("newPhone");
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail");
        userDTO.setPassword("password");
        var userResponseDTO = userService.update(principal, userDTO);
        Assertions.assertEquals(userDTO.getEmail(), userResponseDTO.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), userResponseDTO.getUsername());
        Assertions.assertEquals(userDTO.getPhone(), userResponseDTO.getPhone());
    }

    @Test
    void delete_shouldReturnNull_when_userIsExists() {
        var deleteUser = userService.delete(principal);
        Assertions.assertNull(deleteUser);
    }

}