package com.manager.finance.infrastructure.adapter.in.rest.controller;

import com.manager.finance.application.port.in.CategoryUseCase;
import com.manager.finance.domain.model.CategoryModel;
import com.manager.finance.infrastructure.adapter.in.exception.InvalidUUIDException;
import com.manager.finance.infrastructure.adapter.in.rest.dto.request.CategoryRequestDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.CategoryResponseDTO;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestError;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestResponse;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.finance.infrastructure.adapter.in.rest.mapper.DtoMapper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.UserPrincipalMapper;
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

import static com.manager.finance.constant.Constant.CATEGORY_ENTITY;

@RestController
@RequestMapping("/v1/category")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryUseCase categoryUseCase;
    private final DtoMapper<CategoryRequestDTO, CategoryResponseDTO, CategoryModel> dtoMapper;
    private final ErrorHelper errorHelper;
    private final UserPrincipalMapper principalMapper;

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<RestResponse<List<CategoryResponseDTO>>> getCategories(Principal principal) {
        List<CategoryResponseDTO> categoryResponseDTOS = categoryUseCase.getAll(principalMapper.toModel(principal).id()).stream().map(dtoMapper::toResponseDto).toList();
        RestResponse<List<CategoryResponseDTO>> e = new RestResponse<>(null, categoryResponseDTOS);
        return new ResponseEntity<>(e, HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    @TrackExecutionTime
    public ResponseEntity<RestResponse<List<CategoryResponseDTO>>> getCategoryPage(Principal principal, @PathVariable("page") int page,
                                                                                   @RequestParam(defaultValue = "200") int countPerPage) {
        List<CategoryResponseDTO> categoryResponseDTOS = categoryUseCase.getPage(page, countPerPage).stream().map(dtoMapper::toResponseDto).toList();
        RestResponse<List<CategoryResponseDTO>> e = new RestResponse<>(null, categoryResponseDTOS);
        return new ResponseEntity<>(e, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @TrackExecutionTime
    public ResponseEntity<RestResponse<CategoryResponseDTO>> getCategory(Principal principal, @PathVariable("id") String id) {
        HttpStatus status;
        RestError restError = null;
        CategoryResponseDTO categoryResponseDTO;
        try {
            UUID uuid = UUID.fromString(id);
            var model = categoryUseCase.get(uuid, principalMapper.toModel(principal).id());
            status = HttpStatus.OK;
            categoryResponseDTO = dtoMapper.toResponseDto(model);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(id, CATEGORY_ENTITY);
        }

        RestResponse<CategoryResponseDTO> response = new RestResponse<>(restError, categoryResponseDTO);
        return new ResponseEntity<>(response, status);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<RestResponse<CategoryResponseDTO>> addCategory(Principal principal, @RequestBody @Valid CategoryRequestDTO categoryDTO, BindingResult bindingResult) {
        HttpStatus status;
        RestError restError = null;
        CategoryResponseDTO categoryResponseDTO = null;
        var responseEntity = errorHelper.checkErrors2(bindingResult);
        if (responseEntity == null) {
            status = HttpStatus.OK;
            categoryResponseDTO = dtoMapper.toResponseDto(categoryUseCase.create(principalMapper.toModel(principal).id(), dtoMapper.toModel(categoryDTO)));
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, responseEntity);
        }

        RestResponse<CategoryResponseDTO> response = new RestResponse<>(restError, categoryResponseDTO);
        return new ResponseEntity<>(response, status);

    }

}

