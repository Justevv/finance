package com.manager.finance.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class BaseCrudResponseDTO implements CrudResponseDTO {
    private UUID guid;
}
