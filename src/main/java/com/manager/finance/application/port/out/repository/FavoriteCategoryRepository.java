package com.manager.finance.application.port.out.repository;

import com.manager.finance.domain.model.FavoriteCategoryModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.FavoriteCategoryEntity;
import com.manager.user.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface FavoriteCategoryRepository {
    List<FavoriteCategoryEntity> findByUser(UserEntity userEntity);

    void deleteById(UUID id);

    FavoriteCategoryModel save(FavoriteCategoryModel categoryModel);
}
