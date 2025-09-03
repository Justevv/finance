package com.manager.finance.infrastructure.persistace.repository;

import com.manager.finance.infrastructure.persistace.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository extends JpaRepository<PlaceEntity, UUID> {
    Optional<PlaceEntity> findByName(String name);

    @Modifying
    @Query("DELETE FROM PlaceEntity pe WHERE pe.id = ?1")
    void deleteById(UUID id);
}
