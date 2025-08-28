package com.manager.finance.repository;

import com.manager.finance.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findByName(String name);

    @Modifying
    @Query("DELETE FROM CategoryEntity ce WHERE ce.id = ?1")
    void deleteById(UUID id);
}
