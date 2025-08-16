package com.manager.finance.service;

import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.dto.response.CategoryResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.repository.CategoryRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class CategoryService implements CrudService<CategoryEntity, CategoryDTO, CategoryResponseDTO> {
    @Getter
    private final String entityTypeName;
    private final CrudLogConstants crudLogConstants;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;
    private final UserHelper userHelper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper mapper, UserHelper userHelper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.userHelper = userHelper;
        entityTypeName = "category";
        crudLogConstants = new CrudLogConstants(entityTypeName);
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
        var category = saveAndGet(principal, categoryDTO);
        return convertEntityToResponseDTO(category);
    }

    private CategoryEntity saveAndGet(Principal principal, CategoryDTO categoryDTO) {
        log.debug(crudLogConstants.getInputNewDTO(), categoryDTO);
        var category = mapper.map(categoryDTO, CategoryEntity.class);
//        setDefaultValue(category);
        category.setUser(userHelper.getUser(principal));
        categoryRepository.save(category);
        log.info(crudLogConstants.getSaveEntityToDatabase(), category);
        return category;
    }

    private void setDefaultValue(CategoryEntity category) {
        if (category.getParentCategory() == null) {
            category.setParentCategory(categoryRepository.findByName("Base").get());
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

    public CategoryEntity getOrCreate(Principal principal, CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        } else if (categoryDTO.getId() != null) {
            var category = categoryRepository.findById(categoryDTO.getId());
            if (category.isPresent()) {
                return category.get();
            }
        } else if (categoryDTO.getName() != null) {
            var category = categoryRepository.findByName(categoryDTO.getName());
            return category.orElseGet(() -> saveAndGet(principal, categoryDTO));
        }
        return null;
    }
}


