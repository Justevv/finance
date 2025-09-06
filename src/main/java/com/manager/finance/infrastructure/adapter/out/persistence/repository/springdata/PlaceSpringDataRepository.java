package com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PlaceSpringDataRepository extends JpaRepository<PlaceEntity, UUID> {
    Optional<PlaceEntity> findByName(String name);

    @Modifying
    @Query("DELETE FROM PlaceEntity pe WHERE pe.id = ?1")
    void deleteById(UUID id);
}
