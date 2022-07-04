package com.manager.finance.dto.response;

import com.manager.finance.entity.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseResponseDTO extends BaseCrudResponseDTO {
    private String description;
    private LocalDateTime date;
    private CategoryEntity category;
    private PlaceEntity place;
    private PaymentTypeEntity paymentType;
    private double sum;
    private AccountEntity account;
    private TransactionType transactionType;

}

