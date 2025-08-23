package com.manager.finance.repository;

import com.manager.finance.entity.PlaceEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository extends JpaRepository<PlaceEntity, UUID> {
    Optional<PlaceEntity> findByName(String name);

    List<PlaceEntity> findByUser(UserEntity user);
}
