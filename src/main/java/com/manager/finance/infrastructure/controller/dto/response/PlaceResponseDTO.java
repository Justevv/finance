package com.manager.finance.infrastructure.controller.dto.response;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(timeToLive = 3600L)
public class PlaceResponseDTO extends BaseCrudResponseDTO {
    private String name;
    private String address;
}
