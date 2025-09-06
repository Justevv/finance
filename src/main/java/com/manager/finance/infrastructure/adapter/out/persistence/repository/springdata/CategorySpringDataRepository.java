package com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategorySpringDataRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findByName(String name);

    @Modifying
    @Query("DELETE FROM CategoryEntity ce WHERE ce.id = ?1")
    void deleteById(UUID id);
}
