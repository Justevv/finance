package com.manager.user.infrastructure.adapter.in.scheduler;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.in.ExpiredDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CleanExpiredDataScheduler {
    private final ExpiredDataUseCase expiredDataUseCase;

    @Scheduled(fixedDelay = 1000000)
    @TrackExecutionTime
    public void cleanExpiredEmailVerification() {
        expiredDataUseCase.cleanExpiredEmailVerification();
    }

    @Scheduled(fixedDelay = 1000000)
    @TrackExecutionTime
    public void cleanExpiredPhoneVerification() {
        expiredDataUseCase.cleanExpiredPhoneVerification();
    }

    @Scheduled(fixedDelay = 1000000)
    @TrackExecutionTime
    public void cleanExpiredPasswordResetToken() {
        expiredDataUseCase.cleanExpiredPasswordResetToken();
    }

}