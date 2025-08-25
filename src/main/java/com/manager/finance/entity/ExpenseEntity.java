package com.manager.finance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
@Data
public class ExpenseEntity extends CrudEntity {
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    @ManyToOne
    private CategoryEntity category;
    @ManyToOne
    private PlaceEntity place;
    @ManyToOne
    private PaymentTypeEntity paymentType;
    private double sum;
    @ManyToOne
    private AccountEntity account;
    @Enumerated(EnumType.ORDINAL)
    private TransactionType transactionType;
    @NotNull
    @ManyToOne
    private UserEntity user;

}
