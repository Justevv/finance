package com.manager.finance.repository;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.metric.TrackExecutionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findByName(String name);

    @TrackExecutionTime
    Optional<CategoryEntity> findByGuid(UUID guid);

    @TrackExecutionTime
    boolean existsByGuid(UUID guid);

    @Modifying
    @Query("DELETE FROM CategoryEntity ce WHERE ce.guid = ?1")
    void deleteByGuid(UUID guid);
}
