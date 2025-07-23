package com.manager.finance.entity;

import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "account")
@Data
public class AccountEntity extends CrudEntity {
    @Size(min = 2, message = "Name small size")
    private String name;


}

