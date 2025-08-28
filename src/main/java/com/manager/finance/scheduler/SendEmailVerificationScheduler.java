package com.manager.finance.scheduler;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.service.verification.EmailVerificationKafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class SendEmailVerificationScheduler {
    private final EmailVerificationKafkaService emailVerificationKafkaService;

    @Scheduled(fixedDelayString = "${manager.schedule.send-email.crone}")
    @TrackExecutionTime
    public void sendConfirmEmail() {
        boolean hasRecords = true;
        while (hasRecords){
           hasRecords = emailVerificationKafkaService.sendConfirmEmail();
        };
    }

}