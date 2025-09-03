package com.manager.user.dto;

import lombok.Data;
import lombok.ToString;

import jakarta.validation.constraints.Email;

import static com.manager.user.dto.UserDTO.EMAIL_REGEXP;

@Data
public class UserUpdateDTO{
    private String username;
    @ToString.Exclude
    private String password;
    @Email(message = "email", regexp = EMAIL_REGEXP)
    private String email;
    private String phone;

}
