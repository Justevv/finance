package com.manager.finance.dto;

import lombok.Data;

@Data
public class UserResponseDTO implements CrudResponseDTO {
    private String username;
    private String email;
    private String phone;
}
