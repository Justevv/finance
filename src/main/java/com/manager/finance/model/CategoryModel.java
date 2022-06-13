package com.manager.finance.model;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class CategoryModel extends CrudModel<CategoryEntity, CategoryDTO> {
    private static final String CATEGORY = "category";
    private final LogConstants logConstants = new LogConstants(CATEGORY);
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper = new ModelMapper();

    public CategoryModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryEntity> getCategory() {
        var categoryEntity = categoryRepository.findAll();
        log.debug(logConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public CategoryEntity create(CategoryDTO categoryDTO, Principal principal) {
        log.debug(logConstants.getInputDataNew(), categoryDTO);
        var category = mapper.map(categoryDTO, CategoryEntity.class);
        setDefaultValue(category);
        category.setUser(getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        categoryRepository.save(category);
        log.info(logConstants.getSaveToDatabase(), category);
        return category;
    }

    private void setDefaultValue(CategoryEntity category){
        if (category.getParentCategory() == null){
            category.setParentCategory(categoryRepository.findByName("Base"));
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity category, CategoryDTO categoryDTO) {
        log.debug(logConstants.getInputDataToChange(), category, categoryDTO);
        mapper.map(categoryDTO, category);
        setDefaultValue(category);
        categoryRepository.save(category);
        log.info(logConstants.getUpdatedToDatabase(), category);
        return category;
    }

    @Override
    public Void delete(CategoryEntity categoryEntity) {
        log.debug(logConstants.getInputDataForDelete(), categoryEntity);
        categoryRepository.delete(categoryEntity);
        log.info(logConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


