package com.manager.user.domain.service.verification;

import com.manager.user.application.port.out.repository.EmailVerificationRepository;
import com.manager.user.domain.model.VerificationModel;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.EmailVerificationEntity;
import com.manager.user.event.ConfirmationCompleteEvent;
import com.manager.user.exception.VerificationNotFoundException;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.EmailVerificationSpringDataRepository;
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
public class EmailVerificationService {
    private static final String VERIFICATION_DOES_NOT_EXISTS_ERROR_MESSAGE = "Verification doesn't exists";
    private static final int NUMBER_DIGITS_VERIFICATION_CODE = 6;
    @Value("${confirmationRegistrationCodeExpiredTime}")
    private int confirmationRegistrationCodeExpiredTime;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationSpringDataRepository emailVerificationSpringDataRepository;
    private final UserSpringDataRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final EntityMapper<UserEntity, UserModel> userMapper;


    @Transactional
    @TrackExecutionTime
    public boolean verifyEmail(UUID userId, String code) {
        log.debug("Trying to verify email for userId {}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));

        var emailVerificationCode = emailVerificationRepository.findByUser(userMapper.toModel(user))
                .orElseThrow(() -> new VerificationNotFoundException(VERIFICATION_DOES_NOT_EXISTS_ERROR_MESSAGE));
        if (emailVerificationCode != null && emailVerificationCode.code().equals(code)
                && !emailVerificationCode.isExpire() && !isEmailAlreadyConfirmed(user.getEmail())) {
            user.setEmailConfirmed(true);
            emailVerificationRepository.delete(emailVerificationCode);
            eventPublisher.publishEvent(new ConfirmationCompleteEvent(user));
            log.info("Email for user {} was confirmed successfully", user);
            return true;
        }
        log.debug("Email confirmation for user {} was rejected", user);
        return false;
    }

    @TrackExecutionTime
    public void createAndSaveVerification(UserEntity user) {
        log.debug("Current user is {}", user);
        var verification = createVerificationCode();
        verification.setUser(user);
        emailVerificationSpringDataRepository.save(verification);
        log.info("Saved verificationCode is {}", verification);
    }

    @TrackExecutionTime
    public void createAndSaveVerification(UserModel user) {
        log.debug("Current user is {}", user);
        var verification = createVerificationCode();
        var save = VerificationModel.builder()
                .id(UUID.randomUUID())
                .code(RandomStringUtils.randomNumeric(NUMBER_DIGITS_VERIFICATION_CODE))
                .expireTime(LocalDateTime.now().plusSeconds(confirmationRegistrationCodeExpiredTime))
                .user(user)
                .isSent(false)
                .build();
        emailVerificationRepository.save(save);
        log.info("Saved verificationCode is {}", verification);
    }

    private EmailVerificationEntity createVerificationCode() {
        String code = RandomStringUtils.randomNumeric(NUMBER_DIGITS_VERIFICATION_CODE);
        return new EmailVerificationEntity(code, confirmationRegistrationCodeExpiredTime);
    }

    public boolean isEmailAlreadyConfirmed(String email) {
        var emailConfirmed = userRepository.findByEmailAndIsEmailConfirmed(email, true);
        var isEmailAlreadyConfirmed = emailConfirmed.isPresent();
        log.debug("Is email {} already confirmed: {}", email, isEmailAlreadyConfirmed);
        return isEmailAlreadyConfirmed;
    }

}