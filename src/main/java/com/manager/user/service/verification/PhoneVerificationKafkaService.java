package com.manager.user.service.verification;

import com.manager.user.dto.response.PhoneVerificationResponseDto;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.repository.PhoneVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper mapper;
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
        phoneVerificationEntities.forEach(phoneVerificationEntity -> {
            PhoneVerificationResponseDto responseDto = PhoneVerificationResponseDto.builder()
                    .id(phoneVerificationEntity.getId())
                    .code(phoneVerificationEntity.getCode())
                    .phone(phoneVerificationEntity.getUser().getPhone())
                    .build();
            var future = kafkaTemplate.sendDefault(responseDto.phone(), responseDto);
            phoneVerificationEntity.setSent(true);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Verification successfully sent: {}", phoneVerificationEntity);
                } else {
                    phoneVerificationEntity.setSent(false);
                    phoneVerificationRepository.save(phoneVerificationEntity);
                    log.error("Failed to send verification {} : {}", phoneVerificationEntity, ex.getMessage());
                }
            });
        });
        phoneVerificationRepository.saveAll(phoneVerificationEntities);
        return true;
    }

}
