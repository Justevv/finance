package com.manager.dto;

import com.manager.entity.*;
import lombok.Data;

import java.util.Date;

@Data
public class ExpenseDTO {
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

