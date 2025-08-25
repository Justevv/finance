package com.manager.finance.repository;

import com.manager.finance.entity.FavoriteCategoryEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FavoriteCategoryRepository extends JpaRepository<FavoriteCategoryEntity, UUID> {
    List<FavoriteCategoryEntity> findByUser(UserEntity userEntity);

    @Modifying
    @Query("DELETE FROM FavoriteCategoryEntity fce WHERE fce.guid = ?1")
    void deleteByGuid(UUID guid);
}
