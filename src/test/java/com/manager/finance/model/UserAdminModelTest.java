package com.manager.finance.model;

import com.manager.Manager;
import com.manager.user.administrator.dto.user.UserAdminDTO;
import com.manager.user.administrator.dto.user.UserAdminUpdateDTO;
import com.manager.user.administrator.model.UserAdminModel;
import com.manager.user.entity.UserEntity;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.repository.PhoneVerificationRepository;
import com.manager.user.repository.UserRepository;
import com.manager.user.repository.EmailVerificationRepository;
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

@SpringBootTest(classes = Manager.class)
@Import({UserPrepareHelper.class})
class UserAdminModelTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmailVerificationRepository emailVerificationRepository;
    @MockBean
    private PhoneVerificationRepository phoneVerificationRepository;
    @MockBean
    private Principal principal;
    @Autowired
    private UserAdminModel userAdminModel;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    private UserEntity user;

    @BeforeEach
    private void prepareUser() {
        user = userPrepareHelper.createUser();
    }

    @Test
    void getUser_shouldReturnUser_when_userIsExists() {
        var userEntities = List.of(user);
        Mockito.when(userRepository.findAll()).thenReturn(userEntities);

        var userResponseDTO = userAdminModel.getAll();
        Assertions.assertEquals(user.getEmail(), userResponseDTO.get(0).getEmail());
        Assertions.assertEquals(user.getUsername(), userResponseDTO.get(0).getUsername());
        Assertions.assertEquals(user.getPhone(), userResponseDTO.get(0).getPhone());
        Assertions.assertEquals(user.isEmailConfirmed(), userResponseDTO.get(0).isEmailConfirmed());
        Assertions.assertEquals(user.isPhoneConfirmed(), userResponseDTO.get(0).isPhoneConfirmed());
        Assertions.assertTrue(user.getRoles().containsAll(userResponseDTO.get(0).getRoles()));
    }

    @Test
    void getUsersAllInfo_shouldReturnUserEntity_when_userIsExists() {
        var userEntities = List.of(user);
        Mockito.when(userRepository.findAll()).thenReturn(userEntities);
        var userResponseDTO = userAdminModel.get(user);
        Assertions.assertEquals(user.getEmail(), userResponseDTO.getEmail());
        Assertions.assertEquals(user.getUsername(), userResponseDTO.getUsername());
        Assertions.assertEquals(user.getPhone(), userResponseDTO.getPhone());
        Assertions.assertEquals(user.isEmailConfirmed(), userResponseDTO.isEmailConfirmed());
        Assertions.assertEquals(user.isPhoneConfirmed(), userResponseDTO.isPhoneConfirmed());
        Assertions.assertTrue(user.getRoles().containsAll(userResponseDTO.getRoles()));
    }

    @Test
    void create_shouldReturnUser_when_userDTOIsOk() {
        var userDTO = new UserAdminDTO();
        userDTO.setPhone("newPhone");
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail");
        userDTO.setPassword("password");
        var userResponseDTO = userAdminModel.createAndGetDTO(userDTO);
        Assertions.assertEquals(userDTO.getEmail(), userResponseDTO.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), userResponseDTO.getUsername());
        Assertions.assertEquals(userDTO.getPhone(), userResponseDTO.getPhone());
        Assertions.assertEquals(userDTO.isEmailConfirmed(), userResponseDTO.isEmailConfirmed());
        Assertions.assertEquals(userDTO.isPhoneConfirmed(), userResponseDTO.isPhoneConfirmed());
        Assertions.assertEquals(userDTO.getRoles(), userResponseDTO.getRoles());
    }

    @Test
    void update_shouldReturnUser_when_userDTOIsOk() {
        var userDTO = new UserAdminUpdateDTO();
        userDTO.setPhone("newPhone");
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail");
        userDTO.setPassword("password");
        var userResponseDTO = userAdminModel.update(user, userDTO);
        Assertions.assertEquals(userDTO.getEmail(), userResponseDTO.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), userResponseDTO.getUsername());
        Assertions.assertEquals(userDTO.getPhone(), userResponseDTO.getPhone());
        Assertions.assertEquals(userDTO.isEmailConfirmed(), userResponseDTO.isEmailConfirmed());
        Assertions.assertEquals(userDTO.isPhoneConfirmed(), userResponseDTO.isPhoneConfirmed());
    }

    @Test
    void delete_shouldReturnNull_when_userIsExists() {
        var deleteUser = userAdminModel.delete(user);
        Assertions.assertNull(deleteUser);
    }

}
