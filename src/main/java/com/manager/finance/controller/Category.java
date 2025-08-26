package com.manager.finance.controller;

import com.manager.finance.dto.CategoryDTO;
import com.manager.finance.dto.response.CategoryResponseDTO;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/category")
@Slf4j
public class Category extends CreateReadApiResponse<CategoryDTO, CategoryResponseDTO> {
    private final CategoryService categoryService;

    public Category(CategoryService categoryService) {
        super(categoryService);
        this.categoryService = categoryService;
    }

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<Object> getCategory(Principal principal) {
        return getAll(principal);
    }

    @GetMapping("/page/{page}")
    @TrackExecutionTime
    public List<CategoryResponseDTO> getCategoryPage(Principal principal, @PathVariable("page") int page,
                                                     @RequestParam(defaultValue = "200") int countPerPage) {
        return categoryService.getPage(page, countPerPage);
    }

    @GetMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> getCategory(Principal principal, @PathVariable("id") String guid) {
        return get(guid, principal);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<Object> addCategory(Principal principal, @RequestBody @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        return create(principal, categoryDTO, bindingResult);
    }

}

