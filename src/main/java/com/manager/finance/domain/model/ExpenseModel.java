package com.manager.finance.domain.model;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.AccountEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.PaymentTypeEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ExpenseModel(
        UUID id,
        String description,
        LocalDateTime date,
        CategoryModel category,
        PlaceModel place,
        PaymentTypeEntity paymentType,
        BigDecimal amount,
        AccountEntity account,
        TransactionType transactionType,
        UUID userId
) implements Model {
}
