package com.manager.finance.infrastructure.adapter.in.rest.dto.request;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.AccountEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.TransactionType;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ExpenseRequestDTO(
        UUID id,
        String description,
        LocalDateTime date,
        CategoryRequestDTO category,
        PlaceRequestDTO place,
        PaymentTypeRequestDTO paymentType,
        BigDecimal amount,
        AccountEntity account,
        TransactionType transactionType,
        UserEntity user
) implements RequestDTO {
}



