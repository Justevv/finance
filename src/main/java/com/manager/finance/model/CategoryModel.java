package com.manager.finance.model;

import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.dto.response.CategoryResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class CategoryModel implements CrudModel<CategoryEntity, CategoryDTO, CategoryResponseDTO> {
    private static final String CATEGORY_LOG_NAME = "category";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(CATEGORY_LOG_NAME);
    private final CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserHelper userHelper;

    public CategoryModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponseDTO get(CategoryEntity entity) {
        return convertEntityToResponseDTO(entity);
    }

    @Override
    public List<CategoryResponseDTO> getAll(Principal principal) {
        var user = userHelper.getUser(principal);
        var categoryEntity = categoryRepository.findByUser(user);
        log.debug(crudLogConstants.getListFiltered(), categoryEntity);
        return categoryEntity.stream().map(this::convertEntityToResponseDTO).toList();
    }

    @Override
    public CategoryResponseDTO create(Principal principal, CategoryDTO categoryDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), categoryDTO);
        var category = mapper.map(categoryDTO, CategoryEntity.class);
        setDefaultValue(category);
        category.setUser(userHelper.getUser(principal));
        categoryRepository.save(category);
        log.info(crudLogConstants.getSaveEntityToDatabase(), category);
        return convertEntityToResponseDTO(category);
    }

    private void setDefaultValue(CategoryEntity category) {
        if (category.getParentCategory() == null) {
            category.setParentCategory(categoryRepository.findByName("Base"));
        }
    }

    @Override
    public CategoryResponseDTO update(CategoryEntity category, CategoryDTO categoryDTO) {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), categoryDTO, category);
        mapper.map(categoryDTO, category);
        categoryRepository.save(category);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), category);
        return convertEntityToResponseDTO(category);
    }

    @Override
    public Void delete(CategoryEntity categoryEntity) {
        log.debug(crudLogConstants.getDeleteEntityFromDatabase(), categoryEntity);
        categoryRepository.delete(categoryEntity);
        return null;
    }

    private CategoryResponseDTO convertEntityToResponseDTO(CategoryEntity category) {
        var responseDTO = mapper.map(category, CategoryResponseDTO.class);
        log.debug(crudLogConstants.getOutputDTOAfterMapping(), responseDTO);
        return responseDTO;
    }

}


