package com.manager.finance.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BaseCrudDTO implements CrudDTO {
    private String user;
    private UUID id;

}
