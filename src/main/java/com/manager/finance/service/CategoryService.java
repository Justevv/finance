package com.manager.finance.service;

import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.dto.response.CategoryResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.exception.EntityNotFoundException;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.log.LogConstants;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.repository.CategoryRepository;
import com.manager.finance.repository.FavoriteCategoryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService implements CreateReadService<CategoryDTO, CategoryResponseDTO> {
    private static final String ENTITY_TYPE_NAME = "category";
    private final CategoryRepository categoryRepository;
    private final FavoriteCategoryService favoriteCategoryService;
    private final ModelMapper mapper;
    private final UserHelper userHelper;

    @TrackExecutionTime
    @Override
    public CategoryResponseDTO get(UUID guid, Principal principal) {
        var category = categoryRepository.findById(guid);
        if (category.isPresent()) {
            return convertEntityToResponseDTO(category.get());
        } else {
            throw new EntityNotFoundException(ENTITY_TYPE_NAME, guid);
        }
    }

    @TrackExecutionTime
    @Override
    public List<CategoryResponseDTO> getAll(Principal principal) {
        var categoryEntity = categoryRepository.findAll();
        log.debug(LogConstants.LIST_FILTERED_RESPONSE_DTO, categoryEntity);
        return categoryEntity.stream().map(this::convertEntityToResponseDTO).toList();
    }

    @TrackExecutionTime
    @Override
    @Transactional
    public CategoryResponseDTO create(Principal principal, CategoryDTO categoryDTO) {
        var existedCategory = categoryRepository.findByName(categoryDTO.getName());
        CategoryEntity category = existedCategory.orElseGet(() -> saveAndGet(principal, categoryDTO));
        return convertEntityToResponseDTO(category);
    }

    @TrackExecutionTime
    public CategoryEntity getOrCreate(Principal principal, CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        } else if (categoryDTO.getGuid() != null) {
            var category = categoryRepository.findById(categoryDTO.getGuid());
            if (category.isPresent()) {
                return category.get();
            }
        } else if (categoryDTO.getName() != null) {
            var category = categoryRepository.findByName(categoryDTO.getName());
            return category.orElseGet(() -> saveAndGet(principal, categoryDTO));
        }
        return null;
    }

    private CategoryEntity saveAndGet(Principal principal, CategoryDTO categoryDTO) {
        log.debug(LogConstants.INPUT_NEW_DTO, categoryDTO);
        var category = mapper.map(categoryDTO, CategoryEntity.class);
        category.setGuid(UUID.randomUUID());
        categoryRepository.save(category);
        favoriteCategoryService.save(category, userHelper.getUser(principal));
        log.info(LogConstants.SAVE_ENTITY_TO_DATABASE, category);
        return category;
    }

    private CategoryResponseDTO convertEntityToResponseDTO(CategoryEntity category) {
        var responseDTO = mapper.map(category, CategoryResponseDTO.class);
        log.debug(LogConstants.OUTPUT_DTO_AFTER_MAPPING, responseDTO);
        return responseDTO;
    }

}


