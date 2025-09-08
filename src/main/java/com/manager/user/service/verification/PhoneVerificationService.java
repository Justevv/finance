package com.manager.user.service.verification;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PhoneVerificationEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.event.ConfirmationCompleteEvent;
import com.manager.user.exception.VerificationNotFoundException;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.infrastructure.adapter.out.persistence.repository.EmailVerificationRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.PhoneVerificationRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EmailVerificationRepository emailVerificationRepository;
    private final PhoneVerificationRepository phoneVerificationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @TrackExecutionTime
    public boolean verifyPhone(UUID userId, String code) {
        log.debug("Trying to verify phone for userId {}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));

        var phoneVerificationCode = emailVerificationRepository.findByUser(user)
                .orElseThrow(() -> new VerificationNotFoundException(VERIFICATION_DOES_NOT_EXISTS_ERROR_MESSAGE));
        if (phoneVerificationCode != null && phoneVerificationCode.getCode().equals(code)
                && !phoneVerificationCode.isExpire() && !isPhoneAlreadyConfirmed(user.getPhone())) {
            user.setPhoneConfirmed(true);
            emailVerificationRepository.delete(phoneVerificationCode);
            eventPublisher.publishEvent(new ConfirmationCompleteEvent(user));
            log.info("Phone for user {} was confirmed successfully", user);
            return true;
        }
        log.debug("Phone confirmation for user {} was rejected", user);
        return false;
    }

    @TrackExecutionTime
    public void createAndSaveVerification(UserEntity user) {
        log.debug("Current user is {}", user);
        var verification = createVerificationCode();
        verification.setUser(user);
        phoneVerificationRepository.save(verification);
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