package com.manager.finance.dto;

import com.manager.finance.entity.*;
import lombok.Data;

import java.util.Date;

@Data
public class ExpenseDTO implements DTO {
    private String description;
    private Date date;
    private User user;
    private CategoryEntity category;
    private PlaceEntity place;
    private PaymentTypeEntity paymentType;
    private double sum;
    private AccountEntity account;
    private Type transactionType;

}

