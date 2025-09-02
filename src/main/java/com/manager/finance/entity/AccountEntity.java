package com.manager.finance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Table(name = "account")
@Getter
public class AccountEntity extends CrudEntity {
    @Size(min = 2, message = "Name small size")
    private String name;
    @NotNull
    @ManyToOne
    private UserEntity user;


}

