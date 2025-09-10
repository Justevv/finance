package com.manager.user.infrastructure.adapter.in.rest.controller.user;


import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestError;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestResponse;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.in.UserUseCase;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserRequestDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserUpdateRequestDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.OldUserResponseDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserResponseDto;
import com.manager.user.infrastructure.adapter.in.rest.mapper.DtoMapper;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.UserPrincipalMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;
    private final ErrorHelper errorHelper;
    private final DtoMapper<UserRequestDto, UserResponseDto, UserModel> mapper;
    private final DtoMapper<UserUpdateRequestDto, UserResponseDto, UserModel> mapper2;
    private final UserPrincipalMapper principalMapper;

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<RestResponse<UserResponseDto>> getUser(Principal principal) {
        var user =  mapper.toResponseDto(principalMapper.toModel(principal));
        RestResponse<UserResponseDto> response = new RestResponse<>(null, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<RestResponse<UserResponseDto>> createUser(@RequestBody @Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
        HttpStatus status;
        RestError restError = null;
        UserResponseDto responseDto = null;
        var responseEntity = errorHelper.checkErrors2(bindingResult);
        if (responseEntity == null) {
            status = HttpStatus.OK;
            responseDto = mapper.toResponseDto(userUseCase.create(mapper.toModel(userRequestDto)));
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, responseEntity);
        }

        RestResponse<UserResponseDto> response = new RestResponse<>(restError, responseDto);
        return new ResponseEntity<>(response, status);
    }

    @PutMapping
    @TrackExecutionTime
    public ResponseEntity<RestResponse<UserResponseDto>> updateUser(Principal principal, @RequestBody @Valid UserUpdateRequestDto userRequestDto, BindingResult bindingResult) {
        HttpStatus status;
        RestError restError = null;
        UserResponseDto userResponseDto = null;
        var dtoErrors = errorHelper.checkErrors2(bindingResult);
        if (dtoErrors == null) {
            var model = userUseCase.update(principalMapper.toModel(principal), mapper2.toModel(userRequestDto));
            userResponseDto = mapper.toResponseDto(model);
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, dtoErrors);
        }

        RestResponse<UserResponseDto> restResponse = new RestResponse<>(restError, userResponseDto);
        return new ResponseEntity<>(restResponse, status);
    }

    @DeleteMapping
    @TrackExecutionTime
    public ResponseEntity<Void> deleteUser(Principal principal) {
        userUseCase.delete(principalMapper.toModel(principal));
        return ResponseEntity.ok(null);
    }

}