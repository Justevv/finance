package com.manager.user.infrastructure.adapter.in.scheduler;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.service.verification.PhoneVerificationKafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!devp")
public class SendPhoneVerificationScheduler {
    private final PhoneVerificationKafkaService phoneVerificationKafkaService;

    @Scheduled(fixedDelayString = "${manager.schedule.send-sms.crone}")
    @TrackExecutionTime
    public void sendConfirmPhone() {
        boolean hasRecords = true;
        while (hasRecords) {
            hasRecords = phoneVerificationKafkaService.sendConfirmPhone();
        }
    }

}