package com.manager.finance.infrastructure.controller.dto.request;

import com.manager.finance.infrastructure.persistace.entity.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseRequestDTO extends BaseCrudDTO {
    private String description;
    private LocalDateTime date;
    private CategoryDTO category;
    private PlaceDTO place;
    private PaymentTypeDTO paymentType;
    private double sum;
    private AccountDTO account;
    private TransactionType transactionType;

}

