package com.manager.finance.infrastructure.controller.dto.response;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@Data
public class BaseCrudResponseDTO implements CrudResponseDTO, Serializable {
    @Id
    @Indexed
    private UUID id;
}
