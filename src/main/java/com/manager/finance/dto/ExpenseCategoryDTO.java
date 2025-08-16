package com.manager.finance.dto;

import lombok.Data;

@Data
public class ExpenseCategoryDTO extends BaseCrudDTO {
    private Long id;
    private String name;

}
