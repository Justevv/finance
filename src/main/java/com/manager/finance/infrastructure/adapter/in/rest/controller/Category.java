package com.manager.finance.infrastructure.adapter.in.rest.controller;

import com.manager.finance.application.port.in.CategoryUseCase;
import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.CategoryRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.CategoryResponseDTO;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.finance.infrastructure.adapter.in.rest.mapper.DtoMapper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.helper.UserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/category")
@Slf4j
@RequiredArgsConstructor
public class Category {
    private final CategoryUseCase categoryUseCase;
    private final DtoMapper<CategoryRequestDTO, CategoryResponseDTO, CategoryModel> dtoMapper;
    private final ErrorHelper errorHelper;
    private final UserHelper userHelper;

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<Object> getCategory(Principal principal) {
        return ResponseEntity.ok(categoryUseCase.getAll(principal));
    }

    @GetMapping("/page/{page}")
    @TrackExecutionTime
    public List<CategoryResponseDTO> getCategoryPage(Principal principal, @PathVariable("page") int page,
                                                     @RequestParam(defaultValue = "200") int countPerPage) {
        return categoryUseCase.getPage(page, countPerPage).stream().map(dtoMapper::toResponseDto).toList();
    }

    @GetMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<Object> getCategory(Principal principal, @PathVariable("id") String id) {
        ResponseEntity<Object> responseEntity;
        try {
            UUID uuid = UUID.fromString(id);
            var model = categoryUseCase.get(uuid, principal);
            if (model != null) {
                var response = dtoMapper.toResponseDto(model);
                responseEntity = ResponseEntity.ok(response);
            } else {
                responseEntity = new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            responseEntity = new ResponseEntity<>("Invalid UUID", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<Object> addCategory(Principal principal, @RequestBody @Valid CategoryRequestDTO categoryDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(categoryUseCase.create(userHelper.toModel(principal), dtoMapper.toModel(categoryDTO)));
        }
        return responseEntity;
    }

}

