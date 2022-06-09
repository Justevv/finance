package com.manager.finance.controller;

import com.manager.finance.config.LogConstants;
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
@RequestMapping("/category")
@Slf4j
public class Category extends CrudApiResponse<CategoryModel> {
    private static final String CATEGORY = "category";
    private final LogConstants logConstants = new LogConstants(CATEGORY);
    private final CategoryModel categoryModel;


    public Category(CategoryModel categoryModel) {
        super(categoryModel, CATEGORY);
        this.categoryModel = categoryModel;
    }

    @GetMapping
    public List<CategoryEntity> getCategory() {
        List<CategoryEntity> expenseEntities = categoryModel.getCategory();
        log.debug(logConstants.getListFiltered(), expenseEntities);
        return expenseEntities;
    }

    @GetMapping("{id}")
    public CategoryEntity getCategory(@PathVariable("id") CategoryEntity categoryEntity) {
        log.debug(logConstants.getInput(), categoryEntity);
        return categoryEntity;
    }

    @PostMapping
    public ResponseEntity<Object> addCategory(CategoryDTO categoryDTO, Principal principal, BindingResult bindingResult) throws UserPrincipalNotFoundException {
        return create(categoryDTO, principal, bindingResult);
    }

    @PutMapping("{id}")
    public ResponseEntity changeCategory(@PathVariable("id") CategoryEntity categoryEntity, CategoryDTO categoryDTO,
                                         BindingResult bindingResult) {
        return update(categoryEntity, categoryDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteCategory(@PathVariable("id") CategoryEntity categoryEntity) {
        return delete(categoryEntity);
    }

}

