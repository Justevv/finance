package com.manager.finance.dto.response;

import com.manager.finance.entity.CategoryEntity;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(timeToLive = 3600L)
public class CategoryResponseDTO extends BaseCrudResponseDTO {
    private String name;
    private CategoryEntity parentCategory;

}
