package com.manager.finance.infrastructure.persistace.repository;

import com.manager.finance.infrastructure.persistace.entity.FavoriteCategoryEntity;
import com.manager.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FavoriteCategoryRepository extends JpaRepository<FavoriteCategoryEntity, UUID> {
    List<FavoriteCategoryEntity> findByUser(UserEntity userEntity);

    @Modifying
    @Query("DELETE FROM FavoriteCategoryEntity fce WHERE fce.id = ?1")
    void deleteById(UUID id);
}
