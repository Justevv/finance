package com.manager.finance.dto;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountDTO implements CrudDTO {
    @Size(min = 2, message = "small ")
    private String name;

}

