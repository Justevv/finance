package com.manager.finance.dto;

import lombok.Data;

@Data
public class PlaceDTO extends BaseCrudDTO {
    private String name;
    private String address;
}
