package com.manager.finance.repository;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    //    CategoryEntity findById(long id);
    List<CategoryEntity> findByUser(UserEntity userEntity);

    CategoryEntity findByName(String name);
}
