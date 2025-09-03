package com.manager.finance.infrastructure.controller.dto.request;

import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class AccountDTO extends BaseCrudDTO {
    @Size(min = 2, message = "small ")
    private String name;

}

