package com.manager.finance.infrastructure.adapter.out.persistence.repository.impl;

import com.manager.finance.application.port.out.repository.FavoriteCategoryRepository;
import com.manager.finance.domain.model.FavoriteCategoryModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.FavoriteCategoryEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata.FavoriteCategorySpringDataRepository;
import com.manager.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FavoriteCategorySpringDataRepositoryImplements implements FavoriteCategoryRepository {

    private final FavoriteCategorySpringDataRepository repository;
    private final EntityMapper<FavoriteCategoryEntity, FavoriteCategoryModel> mapper;


    @Override
    public List<FavoriteCategoryEntity> findByUser(UserEntity userEntity) {
        return List.of();
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public FavoriteCategoryModel save(FavoriteCategoryModel categoryModel) {
        var d = mapper.toEntity(categoryModel);
        FavoriteCategoryEntity save = repository.save(d);
        return mapper.toModel(save);
    }

}
