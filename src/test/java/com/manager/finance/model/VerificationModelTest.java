package com.manager.finance.model;

import com.manager.Manager;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.entity.VerificationEntity;
import com.manager.finance.entity.VerificationType;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.repository.VerificationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest(classes = Manager.class)
@Import({UserPrepareHelper.class})
class VerificationModelTest {
    private static final String VERIFICATION_CODE = "100500";
    @Autowired
    private VerificationModel verificationModel;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private VerificationRepository verificationRepository;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    private UserEntity userEntity;
    private VerificationEntity verificationCode;

    @BeforeEach
    private void prepareData() {
        userEntity = userPrepareHelper.createUser();
        verificationCode = new VerificationEntity();
        verificationCode.setId(1);
        verificationCode.setUser(userEntity);
        verificationCode.setCode(VERIFICATION_CODE);
        verificationCode.setExpireTime(LocalDateTime.MAX);
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
    }

    @Test
    void confirmPhone_shouldReturnTrue_when_verificationIsValid() {
        Mockito.when(verificationRepository.findByUserAndType(userEntity, VerificationType.PHONE)).thenReturn(Optional.of(verificationCode));

        Assertions.assertTrue(verificationModel.verifyPhone(userEntity.getId(), VERIFICATION_CODE));
    }

    @Test
    void confirmEmail_shouldReturnTrue_when_verificationIsValid() {
        Mockito.when(verificationRepository.findByUserAndType(userEntity, VerificationType.EMAIL)).thenReturn(Optional.of(verificationCode));

        Assertions.assertTrue(verificationModel.verifyEmail(userEntity.getId(), VERIFICATION_CODE));
    }

    @Test
    void confirmPhone_shouldReturnFalse_when_verificationAlreadyExists() {
        Mockito.when(userRepository.findByPhoneAndIsPhoneConfirmed(userEntity.getPhone(), true)).thenReturn(Optional.of(userEntity));
        Mockito.when(verificationRepository.findByUserAndType(userEntity, VerificationType.PHONE)).thenReturn(Optional.of(verificationCode));

        Assertions.assertFalse(verificationModel.verifyPhone(userEntity.getId(), VERIFICATION_CODE));
    }

    @Test
    void confirmEmail_shouldReturnFalse_when_verificationAlreadyExists() {
        Mockito.when(userRepository.findByEmailAndIsEmailConfirmed(userEntity.getEmail(), true)).thenReturn(Optional.of(userEntity));
        Mockito.when(verificationRepository.findByUserAndType(userEntity, VerificationType.EMAIL)).thenReturn(Optional.of(verificationCode));

        Assertions.assertFalse(verificationModel.verifyEmail(userEntity.getId(), VERIFICATION_CODE));
    }

}