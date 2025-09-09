package com.manager.finance.domain.service;

import com.manager.finance.application.port.out.repository.FavoriteCategoryRepository;
import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.domain.model.FavoriteCategoryModel;
import com.manager.finance.log.LogConstants;
import com.manager.user.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteCategoryService {

    private final FavoriteCategoryRepository repository;

    public void save(CategoryModel category, UserModel user) {
        FavoriteCategoryModel categoryModel = FavoriteCategoryModel.builder()
                .id(UUID.randomUUID())
                .category(category)
                .user(user)
                .build();
        repository.save(categoryModel);
        log.info(LogConstants.SAVE_ENTITY_TO_DATABASE, categoryModel);
    }


    public Void delete(CategoryModel categoryEntity) {
        log.debug(LogConstants.DELETE_ENTITY_FROM_DATABASE, categoryEntity);
//        favoriteCategoryRepository.delete(categoryEntity);
        return null;
    }

}


