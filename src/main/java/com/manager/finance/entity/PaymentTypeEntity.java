package com.manager.finance.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "PaymentType")
@Data
public class PaymentTypeEntity extends CrudEntity {
    private String name;
}
