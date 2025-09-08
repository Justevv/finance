package com.manager.finance.model;

import com.manager.Manager;
import com.manager.user.entity.EmailVerificationEntity;
import com.manager.user.entity.UserEntity;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.repository.UserRepository;
import com.manager.user.repository.EmailVerificationRepository;
import com.manager.user.service.verification.EmailVerificationService;
import com.manager.user.service.verification.PhoneVerificationService;
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
import java.util.UUID;

@SpringBootTest(classes = Manager.class)
@Import({UserPrepareHelper.class})
class EmailVerificationControllerServiceTest {
    private static final String VERIFICATION_CODE = "100500";
    @Autowired
    private PhoneVerificationService phoneVerificationService;
    @Autowired
    private EmailVerificationService emailVerificationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmailVerificationRepository emailVerificationRepository;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    private UserEntity userEntity;
    private EmailVerificationEntity verificationCode;

    @BeforeEach
    private void prepareData() {
        userEntity = userPrepareHelper.createUser();
        verificationCode = new EmailVerificationEntity();
        verificationCode.setId(UUID.randomUUID());
        verificationCode.setUser(userEntity);
        verificationCode.setCode(VERIFICATION_CODE);
        verificationCode.setExpireTime(LocalDateTime.MAX);
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
    }

    @Test
    void confirmPhone_shouldReturnTrue_when_verificationIsValid() {
        Mockito.when(emailVerificationRepository.findByUser(userEntity)).thenReturn(Optional.of(verificationCode));

        Assertions.assertTrue(phoneVerificationService.verifyPhone(userEntity.getId(), VERIFICATION_CODE));
    }

    @Test
    void confirmEmail_shouldReturnTrue_when_verificationIsValid() {
        Mockito.when(emailVerificationRepository.findByUser(userEntity)).thenReturn(Optional.of(verificationCode));

        Assertions.assertTrue(emailVerificationService.verifyEmail(userEntity.getId(), VERIFICATION_CODE));
    }

    @Test
    void confirmPhone_shouldReturnFalse_when_verificationAlreadyExists() {
        Mockito.when(userRepository.findByPhoneAndIsPhoneConfirmed(userEntity.getPhone(), true)).thenReturn(Optional.of(userEntity));
        Mockito.when(emailVerificationRepository.findByUser(userEntity)).thenReturn(Optional.of(verificationCode));

        Assertions.assertFalse(phoneVerificationService.verifyPhone(userEntity.getId(), VERIFICATION_CODE));
    }

    @Test
    void confirmEmail_shouldReturnFalse_when_verificationAlreadyExists() {
        Mockito.when(userRepository.findByEmailAndIsEmailConfirmed(userEntity.getEmail(), true)).thenReturn(Optional.of(userEntity));
        Mockito.when(emailVerificationRepository.findByUser(userEntity)).thenReturn(Optional.of(verificationCode));

        Assertions.assertFalse(emailVerificationService.verifyEmail(userEntity.getId(), VERIFICATION_CODE));
    }

}