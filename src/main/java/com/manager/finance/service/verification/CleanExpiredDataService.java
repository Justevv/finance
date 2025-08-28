package com.manager.finance.service.verification;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.repository.PasswordResetTokenRepository;
import com.manager.finance.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
public class CleanExpiredDataService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    @TrackExecutionTime
    public void cleanExpiredVerification() {
        log.debug("Try to clean expired verify");
        emailVerificationRepository.deleteByExpireTimeBefore(LocalDateTime.now());
        log.debug("Finish remove expired verify");
    }

    @Transactional
    @TrackExecutionTime
    public void cleanExpiredPasswordResetToken() {
        log.debug("Try to clean expired password reset token");
        passwordResetTokenRepository.deleteByExpireTimeBefore(LocalDateTime.now());
        log.debug("Finish remove expired password reset token");
    }

}