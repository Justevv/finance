package com.manager.finance.infrastructure.controller.dto.response;

import com.manager.finance.infrastructure.persistace.entity.CategoryEntity;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(timeToLive = 3600L)
public class CategoryResponseDTO extends BaseCrudResponseDTO {
    private String name;
    private CategoryEntity parentCategory;

}
