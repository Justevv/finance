package com.manager.finance.dto;

import com.manager.finance.entity.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseDTO extends BaseCrudDTO {
    private String description;
    private LocalDateTime date;
    private CategoryDTO category;
    private PlaceDTO place;
    private PaymentTypeDTO paymentType;
    private double sum;
    private AccountDTO account;
    private TransactionType transactionType;

}

