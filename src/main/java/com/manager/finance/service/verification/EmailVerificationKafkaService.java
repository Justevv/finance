package com.manager.finance.service.verification;

import com.manager.finance.dto.response.EmailVerificationResponseDto;
import com.manager.finance.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
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

    @Transactional
    public boolean sendConfirmEmail() {
        var emailVerificationEntities = emailVerificationRepository.findByIsSent(false, Limit.of(batchSize));
        if (emailVerificationEntities.isEmpty()) {
            return false;
        }
        log.debug("Trying to send an email verification messages {}", emailVerificationEntities);
        emailVerificationEntities.stream()
                .map(x -> EmailVerificationResponseDto.builder()
                        .code(x.getCode())
                        .email(x.getUser().getEmail())
                        .build())
                .forEach(x -> kafkaTemplate.sendDefault(x.email(), x));

        emailVerificationEntities.forEach(x -> x.setSent(true));
        emailVerificationRepository.saveAll(emailVerificationEntities);
        return !emailVerificationEntities.isEmpty();
    }

}
