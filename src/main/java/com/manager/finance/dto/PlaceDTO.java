package com.manager.finance.dto;

import lombok.Data;

@Data
public class PlaceDTO implements CrudDTO {
    private String name;
    private String address;
}
