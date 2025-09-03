package com.manager.finance.infrastructure.cache.redis;

import com.manager.finance.infrastructure.controller.dto.response.CategoryResponseDTO;
import com.manager.finance.metric.TrackExecutionTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRedisRepository extends CrudRepository<CategoryResponseDTO, UUID> {

    @TrackExecutionTime
    Optional<CategoryResponseDTO> findByIdAndUserId(UUID id, UUID userId);

}
