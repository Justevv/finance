package com.manager.finance.model;

import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.PhoneVerificationSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.EmailVerificationSpringDataRepository;
import com.manager.user.domain.service.UserService;
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
    private UserSpringDataRepository userRepository;
    @MockBean
    private EmailVerificationSpringDataRepository emailVerificationRepository;
    @MockBean
    private PhoneVerificationSpringDataRepository phoneVerificationRepository;
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
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    }

    @Test
    void getUser_shouldReturnUser_when_principalIsExists() {
        var userResponseDTO = userService.getUser(principal);
        Assertions.assertEquals(user.getEmail(), userResponseDTO.getEmail());
        Assertions.assertEquals(user.getUsername(), userResponseDTO.getUsername());
    }

    @Test
    void create_shouldReturnUser_when_userDTOIsOk() {
        var userDTO = UserModel.builder()
                .phone("newPhone")
                .username("newUsername")
                .email("newEmail")
                .password("password")
                .build();
        var user = userService.create(userDTO);
        Assertions.assertEquals(userDTO.email(), user.email());
        Assertions.assertEquals(userDTO.username(), user.username());
        Assertions.assertEquals(userDTO.phone(), user.phone());
    }

    @Test
    void update_shouldReturnUser_when_userDTOIsOk() {
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var userDTO = UserModel.builder()
                .phone("newPhone")
                .username("newUsername")
                .email("newEmail")
                .password("password")
                .build();

        var principal = UserModel.builder()
                .id(user.getId())
                .build();
        var userResponseDTO = userService.update(principal, userDTO);
        Assertions.assertEquals(userDTO.email(), userResponseDTO.email());
        Assertions.assertEquals(userDTO.username(), userResponseDTO.username());
        Assertions.assertEquals(userDTO.phone(), userResponseDTO.phone());
    }

    @Test
    void delete_shouldReturnNull_when_userIsExists() {
        var principal = UserModel.builder()
            .id(user.getId())
            .build();
        var deleteUser = userService.delete(principal);
        Assertions.assertNull(deleteUser);
    }

}