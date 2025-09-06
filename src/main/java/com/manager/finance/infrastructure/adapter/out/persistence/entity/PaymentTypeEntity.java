package com.manager.finance.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "PaymentType")
@Getter
@ToString
public class PaymentTypeEntity extends CrudEntity {
    private String name;
}
