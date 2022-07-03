package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.entity.VerificationEntity;
import com.manager.finance.entity.VerificationType;
import com.manager.finance.helper.converter.UserIdConverter;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.repository.VerificationRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({UserPrepareHelper.class, UserIdConverter.class})
class VerificationTest {
    private static final String VERIFICATION_CODE = "100500";
    private static final String VERIFICATION_PHONE_API = "/v1/verification/{userId}/phone";
    private static final String VERIFICATION_EMAIL_API = "/v1/verification/{userId}/email";
    private static final String CODE_PARAM_NAME = "code";
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private VerificationRepository verificationRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    private UserEntity userEntity;
    private VerificationEntity verificationCode;

    @BeforeEach
    private void prepareData() {
        userEntity = userPrepareHelper.createUser();
        verificationCode = new VerificationEntity();
        verificationCode.setUser(userEntity);
        verificationCode.setCode(VERIFICATION_CODE);
        verificationCode.setExpireTime(LocalDateTime.MAX);
        Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
    }

    @Test
    @SneakyThrows
    void confirmPhone_shouldReturnTrue_when_verificationIsValid() {
        Mockito.when(verificationRepository.findByUserAndType(userEntity, VerificationType.PHONE)).thenReturn(Optional.of(verificationCode));
        mockMvc.perform(MockMvcRequestBuilders.post(VERIFICATION_PHONE_API, userEntity.getId())
                        .param(CODE_PARAM_NAME, verificationCode.getCode()))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @SneakyThrows
    void confirmEmail_shouldReturnTrue_when_verificationIsValid() {
        Mockito.when(verificationRepository.findByUserAndType(userEntity, VerificationType.EMAIL)).thenReturn(Optional.of(verificationCode));
        mockMvc.perform(MockMvcRequestBuilders.post(VERIFICATION_EMAIL_API, userEntity.getId())
                        .param(CODE_PARAM_NAME, verificationCode.getCode()))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @SneakyThrows
    void confirmPhone_shouldReturnFalse_when_verificationAlreadyExists() {
        Mockito.when(userRepository.findByPhoneAndIsPhoneConfirmed(userEntity.getPhone(), true)).thenReturn(Optional.of(userEntity));
        Mockito.when(verificationRepository.findByUserAndType(userEntity, VerificationType.PHONE)).thenReturn(Optional.of(verificationCode));
        mockMvc.perform(MockMvcRequestBuilders.post(VERIFICATION_PHONE_API, userEntity.getId())
                        .param(CODE_PARAM_NAME, verificationCode.getCode()))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @SneakyThrows
    void confirmEmail_shouldReturnFalse_when_verificationAlreadyExists() {
        Mockito.when(userRepository.findByEmailAndIsEmailConfirmed(userEntity.getEmail(), true)).thenReturn(Optional.of(userEntity));
        Mockito.when(verificationRepository.findByUserAndType(userEntity, VerificationType.EMAIL)).thenReturn(Optional.of(verificationCode));
        mockMvc.perform(MockMvcRequestBuilders.post(VERIFICATION_EMAIL_API, userEntity.getId())
                        .param(CODE_PARAM_NAME, verificationCode.getCode()))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"));
    }
}