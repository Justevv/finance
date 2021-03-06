package com.manager.finance.dto;

import com.manager.finance.entity.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseDTO extends BaseCrudDTO {
    private String description;
    private LocalDateTime date;
    private CategoryEntity category;
    private PlaceEntity place;
    private PaymentTypeEntity paymentType;
    private double sum;
    private AccountEntity account;
    private TransactionType transactionType;

}

