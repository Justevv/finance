package com.manager.finance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PaymentType")
@Data
public class PaymentTypeEntity extends CrudEntity {
    private String name;
}
