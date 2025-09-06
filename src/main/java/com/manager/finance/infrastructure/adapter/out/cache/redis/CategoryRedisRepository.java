package com.manager.finance.infrastructure.adapter.out.cache.redis;

import com.manager.finance.infrastructure.adapter.in.rest.dto.response.CategoryResponseDTO;
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
