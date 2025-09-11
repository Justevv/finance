package com.manager.user.domain.service.verification;

import com.manager.user.application.port.out.repository.PhoneVerificationRepository;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.model.VerificationModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PhoneVerificationEntity;
import com.manager.user.event.ConfirmationCompleteEvent;
import com.manager.user.domain.exception.VerificationNotFoundException;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.PhoneVerificationSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.manager.finance.constant.Constant.USER_DOES_NOT_EXISTS;


@Service
@Slf4j
@RequiredArgsConstructor
public class PhoneVerificationService {
    private static final String VERIFICATION_DOES_NOT_EXISTS_ERROR_MESSAGE = "Verification doesn't exists";
    private static final int NUMBER_DIGITS_VERIFICATION_CODE = 6;
    @Value("${confirmationRegistrationCodeExpiredTime}")
    private int confirmationRegistrationCodeExpiredTime;
    private final PhoneVerificationSpringDataRepository phoneVerificationSpringDataRepository;
    private final PhoneVerificationRepository phoneVerificationRepository;
    private final UserSpringDataRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @TrackExecutionTime
    public boolean verifyPhone(UUID userId, String code) {
        log.debug("Trying to verify phone for userId {}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));

        var phoneVerificationCode = phoneVerificationSpringDataRepository.findByUser(user)
                .orElseThrow(() -> new VerificationNotFoundException(VERIFICATION_DOES_NOT_EXISTS_ERROR_MESSAGE));
        if (phoneVerificationCode != null && phoneVerificationCode.getCode().equals(code)
                && !phoneVerificationCode.isExpire() && !isPhoneAlreadyConfirmed(user.getPhone())) {
            user.setPhoneConfirmed(true);
            phoneVerificationSpringDataRepository.delete(phoneVerificationCode);
            eventPublisher.publishEvent(new ConfirmationCompleteEvent(user));
            log.info("Phone for user {} was confirmed successfully", user);
            return true;
        }
        log.debug("Phone confirmation for user {} was rejected", user);
        return false;
    }

    @TrackExecutionTime
    public void createAndSaveVerification(UserModel user) {
        log.debug("Current user is {}", user);
        var save = VerificationModel.builder()
                .id(UUID.randomUUID())
                .code(RandomStringUtils.randomNumeric(NUMBER_DIGITS_VERIFICATION_CODE))
                .expireTime(LocalDateTime.now().plusSeconds(confirmationRegistrationCodeExpiredTime))
                .user(user)
                .isSent(false)
                .build();
        var saved = phoneVerificationRepository.save(save);
        log.info("Saved verificationCode is {}", saved);
    }

    @TrackExecutionTime
    public void createAndSaveVerification(UserEntity user) {
        log.debug("Current user is {}", user);
        var verification = createVerificationCode();
        verification.setUser(user);
        phoneVerificationSpringDataRepository.save(verification);
        log.info("Saved verificationCode is {}", verification);
    }

    private PhoneVerificationEntity createVerificationCode() {
        String code = RandomStringUtils.randomNumeric(NUMBER_DIGITS_VERIFICATION_CODE);
        return new PhoneVerificationEntity(code, confirmationRegistrationCodeExpiredTime);
    }

    public boolean isPhoneAlreadyConfirmed(String phone) {
        var phones = userRepository.findByPhoneAndIsPhoneConfirmed(phone, true);
        var isPhoneAlreadyConfirmed = phones.isPresent();
        log.debug("Is phone {} already confirmed: {}", phone, isPhoneAlreadyConfirmed);
        return isPhoneAlreadyConfirmed;
    }
}