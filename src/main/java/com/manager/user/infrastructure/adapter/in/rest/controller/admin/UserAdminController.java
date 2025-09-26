package com.manager.user.infrastructure.adapter.in.rest.controller.admin;


import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestError;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestResponse;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.admin.UserAdminService;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserAdminUpdateDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserRequestDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.admin.UserUpdateRequestAdminDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserAdminResponseDTOOld;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserAdminResponseDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserResponseDto;
import com.manager.user.infrastructure.adapter.in.rest.mapper.DtoMapper;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.UserPrincipalMapper;
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

@RestController
@RequestMapping("/v1/admin/user")
@Slf4j
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;
    private final ErrorHelper errorHelper;
    private final DtoMapper<UserRequestDto, UserResponseDto, UserModel> mapper;
    private final DtoMapper<UserUpdateRequestAdminDto, UserAdminResponseDto, UserModel> mapper2;
    private final UserPrincipalMapper principalMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public List<UserAdminResponseDTOOld> getUsers() {
        return userAdminService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public UserAdminResponseDTOOld getUser(@PathVariable("id") UserEntity user) {
        return userAdminService.get(user);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<RestResponse<UserAdminResponseDto>> createUser(@RequestBody @Valid UserUpdateRequestAdminDto userUpdateRequestAdminDto, BindingResult bindingResult) {
        HttpStatus status;
        RestError restError = null;
        UserAdminResponseDto responseDto = null;
        var responseEntity = errorHelper.checkErrors2(bindingResult);
        if (responseEntity == null) {
            status = HttpStatus.OK;
            responseDto = mapper2.toResponseDto(userAdminService.createAndGetDTO(mapper2.toModel(userUpdateRequestAdminDto)));
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, responseEntity);
        }

        RestResponse<UserAdminResponseDto> response = new RestResponse<>(restError, responseDto);
        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") UserEntity user, @Valid UserAdminUpdateDTO userUpdateDTO,
                                             BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(userAdminService.update(user, userUpdateDTO));
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UserEntity user) {
        return ResponseEntity.ok(userAdminService.delete(user));
    }

}

