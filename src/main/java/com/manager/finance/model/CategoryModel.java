package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.repo.CategoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryModel implements CrudModel<CategoryEntity, CategoryDTO> {
    private static final String CATEGORY = "category";
    private final LogConstants logConstants = new LogConstants(CATEGORY);
    private final CategoryRepo categoryRepo;

    public CategoryModel(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<CategoryEntity> getCategory() {
        List<CategoryEntity> categoryEntity = categoryRepo.findAll();
        log.debug(logConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public CategoryEntity create(CategoryDTO categoryDTO) {
        log.debug(logConstants.getInputDataNew(), categoryDTO);
        CategoryEntity categoryEntity = new CategoryEntity(categoryDTO);
        categoryEntity.setParentCategory(categoryDTO.getParentCategory() != null ? categoryDTO.getParentCategory() : categoryRepo.findByName("Base"));
        categoryRepo.save(categoryEntity);
        log.info(logConstants.getSaveToDatabase(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public CategoryEntity update(CategoryEntity categoryEntity, CategoryDTO categoryDTO) {
        log.debug(logConstants.getInputDataToChange(), categoryEntity, categoryDTO);
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setParentCategory(categoryDTO.getParentCategory() != null ? categoryDTO.getParentCategory() : categoryRepo.findByName("Base"));
        categoryRepo.save(categoryEntity);
        log.info(logConstants.getUpdatedToDatabase(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public Void delete(CategoryEntity categoryEntity) {
        log.debug(logConstants.getInputDataForDelete(), categoryEntity);
        categoryRepo.delete(categoryEntity);
        log.info(logConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


