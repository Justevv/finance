package com.manager.finance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ManyToOne
    private UserEntity user;
    @NotNull
    @ManyToOne
    private CategoryEntity category;
    @NotNull
    @ManyToOne
    private PlaceEntity place;
    @ManyToOne
    private PaymentTypeEntity paymentType;
    private double sum;
    @ManyToOne
    private AccountEntity account;
    @Enumerated(EnumType.ORDINAL)
    private Type transactionType;

}
