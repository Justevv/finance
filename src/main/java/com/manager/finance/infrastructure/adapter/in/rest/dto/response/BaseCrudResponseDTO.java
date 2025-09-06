package com.manager.finance.infrastructure.adapter.in.rest.dto.response;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@Data
public class BaseCrudResponseDTO implements ResponseDTO, Serializable {
    @Id
    @Indexed
    private UUID id;
}
