package com.manager.finance.infrastructure.persistace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "PaymentType")
@Getter
public class PaymentTypeEntity extends CrudEntity {
    private String name;
}
