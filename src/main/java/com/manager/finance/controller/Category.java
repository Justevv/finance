package com.manager.finance.controller;

import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.model.CategoryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@Slf4j
public class Category {
    private static final String CATEGORY = "category";
    private final LogConstants logConstants = new LogConstants(CATEGORY);
    private final CategoryModel categoryModel;

    public Category(CategoryModel categoryModel) {
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
    public ResponseEntity<Object> addCategory(CategoryDTO categoryDTO, BindingResult bindingResult) throws IOException {
        log.debug(logConstants.getInputDataNew(), categoryDTO);
        ResponseEntity<Object> responseEntity;

        if (!bindingResult.hasErrors()) {
            CategoryEntity category = categoryModel.createCategory(categoryDTO);
            log.debug(logConstants.getSaveToDatabase(), category);
            responseEntity = ResponseEntity.ok(category);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    @PutMapping("{id}")
    public ResponseEntity changeCategory(@PathVariable("id") CategoryEntity categoryEntity, CategoryDTO categoryDTO,
                                         BindingResult bindingResult) {
        log.debug(logConstants.getInputDataToChange(), categoryDTO, categoryEntity);
        ResponseEntity responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(categoryModel.changeCategory(categoryEntity, categoryDTO));
            log.debug(logConstants.getSaveToDatabase(), categoryDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteCategory(@PathVariable("id") CategoryEntity categoryEntity) {
        log.debug(logConstants.getInputDataForDelete(), categoryEntity);
        ResponseEntity responseEntity = ResponseEntity.ok(categoryModel.deleteCategory(categoryEntity));
        log.debug(logConstants.getDeletedResponse(), responseEntity);
        return responseEntity;
    }

}

