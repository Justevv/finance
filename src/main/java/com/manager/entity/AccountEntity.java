package com.manager.entity;

import com.manager.dto.AccountDTO;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "account")
@Data
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(min = 2, message = "Name small size")
    private String name;

    public AccountEntity() {
    }

    public AccountEntity(AccountDTO bookDTO) {
        this.name = bookDTO.getName();
    }

}

