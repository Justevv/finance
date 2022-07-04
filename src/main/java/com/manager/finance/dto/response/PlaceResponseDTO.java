package com.manager.finance.dto.response;

import lombok.Data;

@Data
public class PlaceResponseDTO extends BaseCrudResponseDTO {
    private String name;
    private String address;
}
