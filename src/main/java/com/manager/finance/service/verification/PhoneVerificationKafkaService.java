package com.manager.finance.service.verification;

import com.manager.finance.dto.response.PhoneVerificationResponseDto;
import com.manager.finance.entity.PhoneVerificationEntity;
import com.manager.finance.repository.PhoneVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public boolean sendConfirmPhone() {
        var phoneVerificationEntities = phoneVerificationRepository.findByIsSent(false, Limit.of(batchSize));
        if (phoneVerificationEntities.isEmpty()) {
            return false;
        }

        log.debug("Trying to send a phone verification messages {}", phoneVerificationEntities);
        List<Pair<CompletableFuture<SendResult<String, PhoneVerificationResponseDto>>, PhoneVerificationEntity>> s = new ArrayList<>();
        phoneVerificationEntities.forEach(phoneVerificationEntity -> {
            PhoneVerificationResponseDto build = PhoneVerificationResponseDto.builder()
                    .code(phoneVerificationEntity.getCode())
                    .phone(phoneVerificationEntity.getUser().getPhone())
                    .build();
            CompletableFuture<SendResult<String, PhoneVerificationResponseDto>> sendResultCompletableFuture = kafkaTemplate.sendDefault(build.phone(), build);
            var z = Pair.of(sendResultCompletableFuture, phoneVerificationEntity);
            s.add(z);
        });

        //                    x.getLeft().get();
        for (Pair<CompletableFuture<SendResult<String, PhoneVerificationResponseDto>>, PhoneVerificationEntity> x : s) {
            x.getLeft().get();
            if (x.getLeft().isDone()) {
                x.getRight().setSent(true);
            }
        }
//        phoneVerificationEntities.forEach(x -> x.setSent(true));
        phoneVerificationRepository.saveAll(phoneVerificationEntities);
        return !phoneVerificationEntities.isEmpty();
    }

}
