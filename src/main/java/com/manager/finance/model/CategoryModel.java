package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.repo.CategoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryModel extends CrudModel<CategoryEntity, CategoryDTO> {
    private static final String CATEGORY = "category";
    private final LogConstants logConstants = new LogConstants(CATEGORY);
    private final CategoryRepo categoryRepo;
    private final ModelMapper mapper = new ModelMapper();

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
        CategoryEntity category = mapper.map(categoryDTO, CategoryEntity.class);
        setDefaultValue(category);
        categoryRepo.save(category);
        log.info(logConstants.getSaveToDatabase(), category);
        return category;
    }

    private void setDefaultValue(CategoryEntity category){
        if (category.getParentCategory() == null){
            category.setParentCategory(categoryRepo.findByName("Base"));
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity category, CategoryDTO categoryDTO) {
        log.debug(logConstants.getInputDataToChange(), category, categoryDTO);
        mapper.map(categoryDTO, category);
        setDefaultValue(category);
        categoryRepo.save(category);
        log.info(logConstants.getUpdatedToDatabase(), category);
        return category;
    }

    @Override
    public Void delete(CategoryEntity categoryEntity) {
        log.debug(logConstants.getInputDataForDelete(), categoryEntity);
        categoryRepo.delete(categoryEntity);
        log.info(logConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


