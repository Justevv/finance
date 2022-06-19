package com.manager.finance.model;

import com.manager.finance.config.CrudLogConstants;
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
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(CATEGORY);
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper = new ModelMapper();

    public CategoryModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryEntity> getAll(Principal principal) {
        var user = getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var categoryEntity = categoryRepository.findByUser(user);
        log.debug(crudLogConstants.getListFiltered(), categoryEntity);
        return categoryEntity;
    }

    @Override
    public CategoryEntity create(CategoryDTO categoryDTO, Principal principal) {
        log.debug(crudLogConstants.getInputDataNew(), categoryDTO);
        var category = mapper.map(categoryDTO, CategoryEntity.class);
        setDefaultValue(category);
        category.setUser(getUserRepository().findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        categoryRepository.save(category);
        log.info(crudLogConstants.getSaveToDatabase(), category);
        return category;
    }

    private void setDefaultValue(CategoryEntity category) {
        if (category.getParentCategory() == null) {
            category.setParentCategory(categoryRepository.findByName("Base"));
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity category, CategoryDTO categoryDTO) {
        log.debug(crudLogConstants.getInputDataToChange(), category, categoryDTO);
        mapper.map(categoryDTO, category);
        categoryRepository.save(category);
        log.info(crudLogConstants.getUpdatedToDatabase(), category);
        return category;
    }

    @Override
    public Void delete(CategoryEntity categoryEntity) {
        log.debug(crudLogConstants.getInputDataForDelete(), categoryEntity);
        categoryRepository.delete(categoryEntity);
        log.info(crudLogConstants.getDeletedFromDatabase(), categoryEntity);
        return null;
    }

}


