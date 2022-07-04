package com.manager.finance.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO extends BaseCrudResponseDTO {
    private String username;
    private String email;
    private String phone;
}
