package com.manager.finance.repository;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUser(UserEntity userEntity);

    Optional<CategoryEntity> findByName(String name);
}
