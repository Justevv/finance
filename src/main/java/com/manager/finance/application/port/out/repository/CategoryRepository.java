package com.manager.finance.application.port.out.repository;

import com.manager.finance.domain.model.CategoryModel;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Optional<CategoryModel> findByName(String name);

    void deleteById(UUID id);

    Optional<CategoryModel> findById(UUID id);

    List<CategoryModel> findAll();

    List<CategoryModel> findAll(Pageable pageable);

    CategoryModel save(CategoryModel category);
}
