package com.manager.user.domain.service.verification;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.out.repository.PhoneVerificationRepository;
import com.manager.user.domain.model.VerificationModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.PhoneVerificationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhoneVerificationKafkaService {
    private final PhoneVerificationRepository phoneVerificationRepository;
    private final KafkaTemplate<String, PhoneVerificationResponseDto> kafkaTemplate;
    @Value("${manager.schedule.send-sms.batch-size}")
    private int batchSize;

    @SneakyThrows
    @Transactional
    @TrackExecutionTime
    public boolean sendConfirmPhone() {
        var phoneVerificationEntities = phoneVerificationRepository.findByIsSent(batchSize);
        if (phoneVerificationEntities.isEmpty()) {
            return false;
        }

        log.debug("Trying to send a phone verification messages {}", phoneVerificationEntities);
        var sentVerification = phoneVerificationEntities.stream()
                .map(toSend -> {
                    var responseDto = PhoneVerificationResponseDto.builder()
                            .id(toSend.id())
                            .code(toSend.code())
                            .phone(toSend.user().phone())
                            .build();
                    var future = kafkaTemplate.sendDefault(responseDto.phone(), responseDto);
                    var verification = VerificationModel.builder()
                            .id(toSend.id())
                            .code(toSend.code())
                            .expireTime(toSend.expireTime())
                            .user(toSend.user())
                            .isSent(true)
                            .build();
                    future.whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.debug("Verification successfully sent: {}", toSend);
                        } else {
                            var unsentVerification = VerificationModel.builder()
                                    .id(toSend.id())
                                    .code(toSend.code())
                                    .expireTime(toSend.expireTime())
                                    .user(toSend.user())
                                    .isSent(false)
                                    .build();
                            phoneVerificationRepository.save(unsentVerification);
                            log.error("Failed to send verification {} : {}", toSend, ex.getMessage());
                        }
                    });
                    return verification;
                })
                .toList();
        phoneVerificationRepository.saveAll(sentVerification);
        return true;
    }

}
