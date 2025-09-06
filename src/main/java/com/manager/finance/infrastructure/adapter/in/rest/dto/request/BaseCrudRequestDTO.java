package com.manager.finance.infrastructure.adapter.in.rest.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class BaseCrudRequestDTO implements RequestDTO {
    private UUID id;

}
