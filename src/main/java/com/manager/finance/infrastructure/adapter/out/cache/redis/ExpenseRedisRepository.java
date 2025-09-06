package com.manager.finance.infrastructure.adapter.out.cache.redis;

import com.manager.finance.application.port.out.cache.ExpenseCache;
import com.manager.finance.domain.model.ExpenseModel;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.ExpenseResponseDTO;
import com.manager.finance.metric.TrackExecutionTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRedisRepository extends CrudRepository<ExpenseResponseDTO, UUID>, ExpenseCache {

    @TrackExecutionTime
    Optional<ExpenseModel> findByIdAndUserId(UUID id, UUID userId);

}
