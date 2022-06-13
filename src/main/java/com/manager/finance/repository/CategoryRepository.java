package com.manager.finance.repository;

import com.manager.finance.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
//    CategoryEntity findById(long id);

    CategoryEntity findByName(String name);
}
