package com.manager.finance.dto;

import lombok.Data;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;

@Data
public class PasswordUpdateDTO {
    @NotBlank
    @ToString.Exclude
    private String password;

}
