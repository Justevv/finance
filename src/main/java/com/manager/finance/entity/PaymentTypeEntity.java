package com.manager.finance.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "PaymentType")
@Data
public class PaymentTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
}
