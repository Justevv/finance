package com.manager.user.service.verification;

import com.manager.user.infrastructure.adapter.in.rest.dto.response.EmailVerificationResponseDto;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.infrastructure.adapter.out.persistence.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationKafkaService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final KafkaTemplate<String, EmailVerificationResponseDto> kafkaTemplate;
    private final ModelMapper mapper;
    @Value("${manager.schedule.send-email.batch-size}")
    private int batchSize;

    @TrackExecutionTime
    @Transactional
    public boolean sendConfirmEmail() {
        var emailVerificationEntities = emailVerificationRepository.findByIsSent((batchSize));
        if (emailVerificationEntities.isEmpty()) {
            return false;
        }
        log.debug("Trying to send an email verification messages {}", emailVerificationEntities);

        emailVerificationEntities.forEach(emailVerificationEntity -> {
            EmailVerificationResponseDto responseDto = EmailVerificationResponseDto.builder()
                    .id(emailVerificationEntity.getId())
                    .code(emailVerificationEntity.getCode())
                    .email(emailVerificationEntity.getUser().getEmail())
                    .build();
            var future = kafkaTemplate.sendDefault(responseDto.email(), responseDto);
            emailVerificationEntity.setSent(true);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Verification successfully sent: {}", emailVerificationEntity);
                } else {
                    emailVerificationEntity.setSent(false);
                    emailVerificationRepository.save(emailVerificationEntity);
                    log.error("Failed to send verification {} : {}", emailVerificationEntity, ex.getMessage());
                }
            });
        });
        emailVerificationRepository.saveAll(emailVerificationEntities);
        return true;
    }

}
