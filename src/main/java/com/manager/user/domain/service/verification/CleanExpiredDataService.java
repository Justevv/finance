package com.manager.user.domain.service.verification;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.EmailVerificationSpringDataRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.PasswordResetTokenRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.PhoneVerificationSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
public class CleanExpiredDataService {
    private final EmailVerificationSpringDataRepository emailVerificationRepository;
    private final PhoneVerificationSpringDataRepository phoneVerificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    @TrackExecutionTime
    public void cleanExpiredEmailVerification() {
        log.debug("Try to clean expired email verify");
        emailVerificationRepository.deleteByExpireTimeBefore(LocalDateTime.now());
        log.debug("Finish remove expired email verify");
    }

    @Transactional
    @TrackExecutionTime
    public void cleanExpiredPhoneVerification() {
        log.debug("Try to clean expired phone verify");
        phoneVerificationRepository.deleteByExpireTimeBefore(LocalDateTime.now());
        log.debug("Finish remove expired phone verify");
    }

    @Transactional
    @TrackExecutionTime
    public void cleanExpiredPasswordResetToken() {
        log.debug("Try to clean expired password reset token");
        passwordResetTokenRepository.deleteByExpireTimeBefore(LocalDateTime.now());
        log.debug("Finish remove expired password reset token");
    }

}