package com.manager.finance.scheduler;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.service.verification.PhoneVerificationKafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
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