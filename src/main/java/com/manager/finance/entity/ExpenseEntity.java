package com.manager.finance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
@Getter
@Setter
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
