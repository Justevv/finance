package com.manager.finance.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "account")
@Data
public class AccountEntity extends CrudEntity {
    @Size(min = 2, message = "Name small size")
    private String name;


}

