package com.manager.finance.controller;

import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.dto.response.CategoryResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.model.CategoryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/v1/category")
@Slf4j
public class Category extends CrudApiResponse<CategoryEntity, CategoryDTO, CategoryResponseDTO> {
    public Category(CategoryModel categoryModel) {
        super(categoryModel);
    }

    @GetMapping
    public ResponseEntity<Object> getCategory(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getCategory(Principal principal, @PathVariable("id") CategoryEntity categoryEntity) {
        return get(principal, categoryEntity);
    }

    @PostMapping
    public ResponseEntity<Object> addCategory(Principal principal, @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        return create(principal, categoryDTO, bindingResult);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> changeCategory(Principal principal, @PathVariable("id") CategoryEntity categoryEntity,
                                                 @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        return update(principal, categoryEntity, categoryDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteCategory(Principal principal, @PathVariable("id") CategoryEntity categoryEntity) {
        return delete(principal, categoryEntity);
    }

}

