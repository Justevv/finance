package com.manager.finance.repo;

import com.manager.finance.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<CategoryEntity, Long> {
//    CategoryEntity findById(long id);

    CategoryEntity findByName(String name);
}
