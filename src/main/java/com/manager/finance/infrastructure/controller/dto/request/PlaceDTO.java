package com.manager.finance.infrastructure.controller.dto.request;

import lombok.Data;

@Data
public class PlaceDTO extends BaseCrudDTO {
    private String name;
    private String address;
}
