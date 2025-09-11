package com.manager.user.infrastructure.adapter.in.scheduler;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.service.verification.EmailVerificationKafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!devp")
public class SendEmailVerificationScheduler {
    private final EmailVerificationKafkaService emailVerificationKafkaService;

    @Scheduled(fixedDelayString = "${manager.schedule.send-email.crone}")
    @TrackExecutionTime
    public void sendConfirmEmail() {
        boolean hasRecords = true;
        while (hasRecords) {
            hasRecords = emailVerificationKafkaService.sendConfirmEmail();
        }
    }

}