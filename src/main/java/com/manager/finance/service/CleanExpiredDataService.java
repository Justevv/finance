package com.manager.finance.service;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.repository.PasswordResetTokenRepository;
import com.manager.finance.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
public class CleanExpiredDataService {
    private final VerificationRepository verificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    @TrackExecutionTime
    public void cleanExpiredVerification() {
        log.debug("Try to clean expired verify");
        verificationRepository.deleteByExpireTimeBefore(LocalDateTime.now());
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