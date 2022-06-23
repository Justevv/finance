package com.manager.finance.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordUpdateDTO implements CrudDTO {
    @NotBlank
    @ToString.Exclude
    private String password;

}
