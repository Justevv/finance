package com.manager.finance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manager.finance.dto.ExpenseDTO;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
@Data
public class ExpenseEntity implements CrudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private CategoryEntity category;
    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private PlaceEntity place;
    @OneToOne(fetch = FetchType.EAGER)
    private PaymentTypeEntity paymentType;
    private double sum;
    @OneToOne(fetch = FetchType.EAGER)
    private AccountEntity account;
    @Enumerated(EnumType.ORDINAL)
    private Type transactionType;

    public ExpenseEntity() {
    }

    public ExpenseEntity(ExpenseDTO expenseDTO) {
        this.description = expenseDTO.getDescription();
        this.date = LocalDateTime.now();
        this.user = expenseDTO.getUser();
        this.category = expenseDTO.getCategory();
        this.place = expenseDTO.getPlace();
        this.paymentType = expenseDTO.getPaymentType();
        this.sum = expenseDTO.getSum();
        this.account = expenseDTO.getAccount();
        this.transactionType = expenseDTO.getTransactionType();
    }
}
