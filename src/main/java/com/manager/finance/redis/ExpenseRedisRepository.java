package com.manager.finance.redis;

import com.manager.finance.dto.response.ExpenseResponseDTO;
import com.manager.finance.metric.TrackExecutionTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRedisRepository extends CrudRepository<ExpenseResponseDTO, UUID> {

    @TrackExecutionTime
    Optional<ExpenseResponseDTO> findByGuidAndUserGuid(UUID guid, UUID userGuid);

}
