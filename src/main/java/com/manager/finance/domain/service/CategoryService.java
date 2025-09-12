package com.manager.finance.domain.service;

import com.manager.finance.application.port.in.CategoryUseCase;
import com.manager.finance.application.port.out.repository.CategoryRepository;
import com.manager.finance.domain.exception.EntityNotFoundException;
import com.manager.finance.domain.exception.SaveProcessException;
import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.log.LogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {
    private static final String ENTITY_TYPE_NAME = "category";
    private final CategoryRepository categoryRepository;
    //    private final CategoryRedisRepository categoryRedisRepository;
    private final FavoriteCategoryService favoriteCategoryService;
    private final ModelMapper mapper;
    private final UserHelper userHelper;

    @TrackExecutionTime
    @Override
    public CategoryModel get(UUID id, UUID userId) {
        var category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new EntityNotFoundException(ENTITY_TYPE_NAME, id);
        }
    }

    @TrackExecutionTime
    @Override
    public List<CategoryModel> getAll(UUID userId) {
        var categories = categoryRepository.findAll();
        log.debug(LogConstants.LIST_FILTERED_RESPONSE_DTO, categories);
        return categories;
    }

    @TrackExecutionTime
    public List<CategoryModel> getPage(int page, int countPerPage) {
        Pageable pageable = PageRequest.of(page, countPerPage);
        var categories = categoryRepository.findAll(pageable);
        log.debug(LogConstants.LIST_FILTERED_RESPONSE_DTO, categories);
        return categories;
    }

    @TrackExecutionTime
    @Override
    @Transactional
    public CategoryModel create(UUID userId, CategoryModel categoryModel) {
        var existedCategory = categoryRepository.findByName(categoryModel.name());
        var category = existedCategory.orElseGet(() -> saveAndGet(userId, categoryModel));
        log.debug("Category {}", category);
        return category;
    }

    @TrackExecutionTime
    public CategoryModel getOrCreate(UUID userId, CategoryModel categoryModel) {
        if (categoryModel == null) {
            return null;
        } else if (categoryModel.id() != null) {
            var category = categoryRepository.findById(categoryModel.id());
            if (category.isPresent()) {
                return category.get();
            }
        } else if (categoryModel.name() != null) {
            var category = categoryRepository.findByName(categoryModel.name());
            return category.orElseGet(() -> saveAndGet(userId, categoryModel));
        }
        return null;
    }

    private CategoryModel saveAndGet(UUID userId, CategoryModel input) {
        log.debug(LogConstants.INPUT_NEW_DTO, input);
        var save = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name(input.name())
                .parentCategory(input.parentCategory())
                .build();
        var saved = categoryRepository.save(save);
        if (saved == null) {
            throw new SaveProcessException(save);
        }
        favoriteCategoryService.save(saved, userId);
        log.info(LogConstants.SAVE_ENTITY_TO_DATABASE, saved);
        return saved;
    }

}


