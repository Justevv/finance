package com.manager.finance.infrastructure.adapter.in.rest.dto.request;

import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class AccountRequestDTO extends BaseCrudRequestDTO {
    @Size(min = 2, message = "small ")
    private String name;

}

