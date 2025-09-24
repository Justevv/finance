package com.manager.user.domain.service.verification;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.out.repository.EmailVerificationRepository;
import com.manager.user.domain.model.VerificationModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.EmailVerificationResponseDto;
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

        var sentVerification = emailVerificationEntities.stream()
                .map(toSend -> {
                    EmailVerificationResponseDto responseDto = EmailVerificationResponseDto.builder()
                            .id(toSend.id())
                            .code(toSend.code())
                            .email(toSend.user().email())
                            .build();
                    var future = kafkaTemplate.sendDefault(responseDto.email(), responseDto);
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
                            emailVerificationRepository.save(unsentVerification);
                            log.error("Failed to send verification {} : {}", toSend, ex.getMessage());
                        }
                    });
                    return verification;
                })
                .toList();
        emailVerificationRepository.saveAll(sentVerification);
        return true;
    }

}
