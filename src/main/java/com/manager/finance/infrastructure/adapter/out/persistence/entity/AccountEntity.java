package com.manager.finance.infrastructure.adapter.out.persistence.entity;

import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "account")
@Getter
@ToString
public class AccountEntity extends CrudEntity {
    @Size(min = 2, message = "Name small size")
    private String name;
    @NotNull
    @ManyToOne
    private UserEntity user;


}

