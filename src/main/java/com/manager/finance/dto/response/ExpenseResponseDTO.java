package com.manager.finance.dto.response;

import com.manager.finance.entity.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseResponseDTO extends BaseCrudResponseDTO {
    private String description;
    private LocalDateTime date;
    private CategoryResponseDTO category;
    private PlaceResponseDTO place;
    private PaymentTypeResponseDTO paymentType;
    private double sum;
    private AccountResponseDTO account;
    private TransactionType transactionType;

}

