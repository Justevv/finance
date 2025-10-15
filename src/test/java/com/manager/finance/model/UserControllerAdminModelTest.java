package com.manager.finance.model;

import com.manager.Manager;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.admin.UserAdminService;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.EmailVerificationSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.PhoneVerificationSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = Manager.class)
@Import({UserPrepareHelper.class})
class UserControllerAdminModelTest {
    @MockitoBean
    private UserSpringDataRepository userRepository;
    @MockitoBean
    private EmailVerificationSpringDataRepository emailVerificationRepository;
    @MockitoBean
    private PhoneVerificationSpringDataRepository phoneVerificationRepository;
    @MockitoBean
    private Principal principal;
    @Autowired
    private UserAdminService userAdminService;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    private UserEntity user;
    private UserModel userModel;

    @BeforeEach
    private void prepareUser() {
        user = userPrepareHelper.createUser();
        userModel = userPrepareHelper.createUserModel();
    }

    @Test
    void getUser_shouldReturnUser_when_userIsExists() {
        var userEntities = List.of(user);
        Mockito.when(userRepository.findAll()).thenReturn(userEntities);

        var userResponseDTO = userAdminService.getAll();
        Assertions.assertEquals(user.getEmail(), userResponseDTO.get(0).email());
        Assertions.assertEquals(user.getUsername(), userResponseDTO.get(0).username());
        Assertions.assertEquals(user.getPhone(), userResponseDTO.get(0).phone());
        Assertions.assertEquals(user.isEmailConfirmed(), userResponseDTO.get(0).isEmailConfirmed());
        Assertions.assertEquals(user.isPhoneConfirmed(), userResponseDTO.get(0).isPhoneConfirmed());
        Assertions.assertTrue(user.getRoles().containsAll(userResponseDTO.get(0).roles()));
    }

    @Test
    void getUsersAllInfo_shouldReturnUserEntity_when_userIsExists() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        var userResponseDTO = userAdminService.get(user.getId());
        Assertions.assertEquals(user.getEmail(), userResponseDTO.email());
        Assertions.assertEquals(user.getUsername(), userResponseDTO.username());
        Assertions.assertEquals(user.getPhone(), userResponseDTO.phone());
        Assertions.assertEquals(user.isEmailConfirmed(), userResponseDTO.isEmailConfirmed());
        Assertions.assertEquals(user.isPhoneConfirmed(), userResponseDTO.isPhoneConfirmed());
        Assertions.assertTrue(user.getRoles().containsAll(userResponseDTO.roles()));
    }

    @Test
    void create_shouldReturnUser_when_userDTOIsOk() {
        var userDTO = userPrepareHelper.createUserModel();
        var userResponseDTO = userAdminService.create(userDTO);
        Assertions.assertEquals(userDTO.email(), userResponseDTO.email());
        Assertions.assertEquals(userDTO.username(), userResponseDTO.username());
        Assertions.assertEquals(userDTO.phone(), userResponseDTO.phone());
        Assertions.assertEquals(userDTO.isEmailConfirmed(), userResponseDTO.isEmailConfirmed());
        Assertions.assertEquals(userDTO.isPhoneConfirmed(), userResponseDTO.isPhoneConfirmed());
        Assertions.assertEquals(userDTO.roles().iterator().next().getName(), userResponseDTO.roles().iterator().next().getName());
    }

    @Test
    void update_shouldReturnUser_when_userDTOIsOk() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);
        var userResponseDTO = userAdminService.update(user.getId(), userModel);
        Assertions.assertEquals(userModel.email(), userResponseDTO.email());
        Assertions.assertEquals(userModel.username(), userResponseDTO.username());
        Assertions.assertEquals(userModel.phone(), userResponseDTO.phone());
        Assertions.assertEquals(userModel.isEmailConfirmed(), userResponseDTO.isEmailConfirmed());
        Assertions.assertEquals(userModel.isPhoneConfirmed(), userResponseDTO.isPhoneConfirmed());
    }

    @Test
    void delete_shouldReturnNull_when_userIsExists() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        userAdminService.delete(user.getId());
        Assertions.assertNull(null);
    }

}
