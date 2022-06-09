package com.manager.finance.dto;

import lombok.Data;

@Data
public class UserDTO implements DTO{
    private String username;

    public UserDTO(String username) {
        this.username = username;
    }
}
