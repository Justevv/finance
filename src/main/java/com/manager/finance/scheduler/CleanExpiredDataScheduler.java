package com.manager.finance.scheduler;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.service.CleanExpiredDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CleanExpiredDataScheduler {
    private final CleanExpiredDataService cleanExpiredDataService;

    @Scheduled(fixedDelay = 10000)
    @TrackExecutionTime
    public void cleanExpiredVerification() {
         cleanExpiredDataService.cleanExpiredVerification();
    }

    @Scheduled(fixedDelay = 10000)
    @TrackExecutionTime
    public void cleanExpiredPasswordResetToken() {
        cleanExpiredDataService.cleanExpiredPasswordResetToken();
    }

}