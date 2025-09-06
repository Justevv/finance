package com.manager.finance.infrastructure.adapter.in.rest.dto.request;

import lombok.Data;

@Data
public class ExpenseCategoryRequestDTO extends BaseCrudRequestDTO {
    private String name;

}
