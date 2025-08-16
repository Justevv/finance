package com.manager.finance.controller;

import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.dto.response.CategoryResponseDTO;
import com.manager.finance.entity.CategoryEntity;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/v1/category")
@Slf4j
public class Category extends CrudApiResponse<CategoryEntity, CategoryDTO, CategoryResponseDTO> {
    public Category(CategoryService categoryService) {
        super(categoryService);
    }

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<Object> getCategory(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> getCategory(Principal principal, @PathVariable("id") CategoryEntity categoryEntity) {
        return get(principal, categoryEntity);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<Object> addCategory(Principal principal, @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        return create(principal, categoryDTO, bindingResult);
    }

    @PutMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> changeCategory(Principal principal, @PathVariable("id") CategoryEntity categoryEntity,
                                                 @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        return update(principal, categoryEntity, categoryDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> deleteCategory(Principal principal, @PathVariable("id") CategoryEntity categoryEntity) {
        return delete(principal, categoryEntity);
    }

}

