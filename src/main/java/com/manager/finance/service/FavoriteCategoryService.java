package com.manager.finance.service;

import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.entity.FavoriteCategoryEntity;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.log.LogConstants;
import com.manager.finance.repository.FavoriteCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteCategoryService {

    private final FavoriteCategoryRepository favoriteCategoryRepository;

    public void save(CategoryEntity category, UserEntity user) {
        FavoriteCategoryEntity favoriteCategory = FavoriteCategoryEntity.builder()
                .id(UUID.randomUUID())
                .category(category)
                .user(user)
                .build();
        favoriteCategoryRepository.save(favoriteCategory);
        log.info(LogConstants.SAVE_ENTITY_TO_DATABASE, favoriteCategory);
    }


    public Void delete(CategoryEntity categoryEntity) {
        log.debug(LogConstants.DELETE_ENTITY_FROM_DATABASE, categoryEntity);
//        favoriteCategoryRepository.delete(categoryEntity);
        return null;
    }

}


