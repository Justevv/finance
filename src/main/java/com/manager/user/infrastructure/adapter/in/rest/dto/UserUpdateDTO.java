package com.manager.user.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.ToString;

import static com.manager.finance.infrastructure.adapter.in.rest.dto.validator.EmailValidator.EMAIL_REGEXP;

@Data
public class UserUpdateDTO {
    private String username;
    @ToString.Exclude
    private String password;
    @Email(message = "email", regexp = EMAIL_REGEXP)
    private String email;
    private String phone;

}
