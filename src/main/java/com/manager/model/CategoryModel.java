package com.manager.model;

import com.manager.config.LogConstants;
import com.manager.dto.CategoryDTO;
import com.manager.entity.CategoryEntity;
import com.manager.repo.CategoryRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryModel {
    private static final Logger LOGGER = LogManager.getLogger(CategoryModel.class);
    private static final String CATEGORY = "category";
    private final LogConstants logConstants = new LogConstants(CATEGORY);
    private final CategoryRepo categoryRepo;

    public CategoryModel(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<CategoryEntity> getCategory() {
        List<CategoryEntity> categoryEntity = categoryRepo.findAll();
        LOGGER.debug(logConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    public CategoryEntity createCategory(CategoryDTO categoryDTO) {
        LOGGER.debug(logConstants.getInputDataNew(), categoryDTO);
        CategoryEntity categoryEntity = new CategoryEntity(categoryDTO);
        categoryEntity.setParentCategory(categoryDTO.getParentCategory() != null ? categoryDTO.getParentCategory() : categoryRepo.findByName("Base"));
        categoryRepo.save(categoryEntity);
        LOGGER.info(logConstants.getSaveToDatabase(), categoryEntity);
        return categoryEntity;
    }

    public CategoryEntity changeCategory(CategoryEntity categoryEntity, CategoryDTO categoryDTO) {
        LOGGER.debug(logConstants.getInputDataToChange(), categoryEntity, categoryDTO);
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setParentCategory(categoryDTO.getParentCategory() != null ? categoryDTO.getParentCategory() : categoryRepo.findByName("Base"));
        categoryRepo.save(categoryEntity);
        LOGGER.info(logConstants.getUpdatedToDatabase(), categoryEntity);
        return categoryEntity;
    }

    public Void deleteCategory(CategoryEntity categoryEntity) {
        LOGGER.debug(logConstants.getInputDataForDelete(), categoryEntity);
        categoryRepo.delete(categoryEntity);
        LOGGER.info(logConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


