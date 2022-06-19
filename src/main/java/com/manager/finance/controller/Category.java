package com.manager.finance.controller;

import com.manager.finance.config.CrudLogConstants;
import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.model.CategoryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/category")
@Slf4j
public class Category extends CrudApiResponse<CategoryModel, CategoryEntity> {
    private static final String CATEGORY = "category";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(CATEGORY);


    public Category(CategoryModel categoryModel) {
        super(categoryModel, CATEGORY);
    }

    @GetMapping
    public List<CategoryEntity> getCategory(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("{id}")
    public CategoryEntity getCategory(@PathVariable("id") CategoryEntity categoryEntity) {
        log.debug(crudLogConstants.getInput(), categoryEntity);
        return categoryEntity;
    }

    @PostMapping
    public ResponseEntity<Object> addCategory(CategoryDTO categoryDTO, Principal principal, BindingResult bindingResult)
            throws UserPrincipalNotFoundException {
        return create(categoryDTO, principal, bindingResult);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> changeCategory(@PathVariable("id") CategoryEntity categoryEntity, CategoryDTO categoryDTO,
                                         BindingResult bindingResult) {
        return update(categoryEntity, categoryDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") CategoryEntity categoryEntity) {
        return delete(categoryEntity);
    }

}

