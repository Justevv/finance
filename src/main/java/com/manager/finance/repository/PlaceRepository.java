package com.manager.finance.repository;

import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.metric.TrackExecutionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository extends JpaRepository<PlaceEntity, UUID> {
    Optional<PlaceEntity> findByName(String name);

    @TrackExecutionTime
    Optional<PlaceEntity> findByGuid(UUID guid);

    @TrackExecutionTime
    boolean existsByGuid(UUID guid);

    @Modifying
    @Query("DELETE FROM PlaceEntity pe WHERE pe.guid = ?1")
    void deleteByGuid(UUID guid);
}
