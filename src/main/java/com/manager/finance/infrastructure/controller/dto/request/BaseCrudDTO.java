package com.manager.finance.infrastructure.controller.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class BaseCrudDTO implements CrudDTO {
    private String user;
    private UUID id;

}
