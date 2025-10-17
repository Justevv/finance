package com.manager.user.infrastructure.adapter.in.rest.controller.admin;


import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestError;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestResponse;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.user.domain.model.RoleModel;
import com.manager.user.domain.service.admin.RoleService;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.OldRoleDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.RoleRequestDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.RoleResponseDto;
import com.manager.user.infrastructure.adapter.in.rest.mapper.DtoMapper;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/role")
@Slf4j
@RequiredArgsConstructor
public class Role {
    private final RoleService roleService;
    private final ErrorHelper errorHelper;
    private final DtoMapper<RoleRequestDto, RoleResponseDto, RoleModel> mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<RestResponse<List<RoleResponseDto>>> getRoles() {
        var responseDto = roleService.getAll().stream().map(mapper::toResponseDto).toList();

        RestResponse<List<RoleResponseDto>> response = new RestResponse<>(null, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<RestResponse<RoleResponseDto>> getRole(@PathVariable("id") UUID id) {
        var responseDto = mapper.toResponseDto(roleService.get(id));

        RestResponse<RoleResponseDto> response = new RestResponse<>(null, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> createRole(@RequestBody @Valid RoleRequestDto role, BindingResult bindingResult) {
        HttpStatus status;
        RestError restError = null;
        RoleResponseDto responseDto = null;
        var responseEntity = errorHelper.checkErrors2(bindingResult);
        if (responseEntity == null) {
            status = HttpStatus.OK;
            responseDto = mapper.toResponseDto(roleService.create(mapper.toModel(role)));
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, responseEntity);
        }

        RestResponse<RoleResponseDto> response = new RestResponse<>(restError, responseDto);
        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> updateRole(@PathVariable("id") RoleEntity role, @Valid OldRoleDTO roleDTO,
                                             BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(roleService.update(role, roleDTO));
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") RoleEntity role) {
        return ResponseEntity.ok(roleService.delete(role));
    }

}

