package com.manager.finance.dto.user;

import com.manager.finance.dto.CrudResponseDTO;
import lombok.Data;

@Data
public class UserResponseDTO implements CrudResponseDTO {
    private String username;
    private String email;
    private String phone;
}
