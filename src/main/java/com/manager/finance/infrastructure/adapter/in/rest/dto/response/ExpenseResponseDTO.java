package com.manager.finance.infrastructure.adapter.in.rest.dto.response;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.TransactionType;
import com.manager.user.entity.UserEntity;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ExpenseResponseDTO(
        UUID id,
        String description,
        LocalDateTime date,
        CategoryResponseDTO category,
        PlaceResponseDTO place,
        PaymentTypeResponseDTO paymentType,
        BigDecimal amount,
        AccountResponseDTO account,
        TransactionType transactionType,
        UserEntity user
) implements ResponseDTO {
}

