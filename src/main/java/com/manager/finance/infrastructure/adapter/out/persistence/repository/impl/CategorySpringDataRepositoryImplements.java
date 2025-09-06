package com.manager.finance.infrastructure.adapter.out.persistence.repository.impl;

import com.manager.finance.application.port.out.repository.CategoryRepository;
import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata.CategorySpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CategorySpringDataRepositoryImplements implements CategoryRepository {

    private final CategorySpringDataRepository repository;
    private final EntityMapper<CategoryEntity, CategoryModel> mapper;

    @Override
    public Optional<CategoryModel> findByName(String name) {
        return repository.findByName(name).map(mapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public Optional<CategoryModel> findById(UUID id) {
        Optional<CategoryEntity> category = repository.findById(id);
        return category.map(mapper::toModel);
    }

    @Override
    public List<CategoryModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public List<CategoryModel> findAll(Pageable pageable) {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public CategoryModel save(CategoryModel category) {
        CategoryEntity entity = mapper.toEntity(category);
        CategoryEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }
}
