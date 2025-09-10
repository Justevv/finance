package com.manager.user.infrastructure.adapter.in.rest.controller.user;


import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestError;
import com.manager.finance.infrastructure.adapter.in.rest.dto.response.RestResponse;
import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.UserService;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserRequestDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserRequestUpdateDTO;
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
    private final UserService userService;
    private final ErrorHelper errorHelper;
    private final DtoMapper<UserRequestDTO, UserResponseDto, UserModel> mapper;
    private final DtoMapper<UserRequestUpdateDTO, UserResponseDto, UserModel> mapper2;
    private final UserPrincipalMapper principalMapper;

    @GetMapping
    @TrackExecutionTime
    public OldUserResponseDTO getUser(Principal principal) {
        return userService.getUser(principal);
    }

    @PostMapping
    @TrackExecutionTime
    public ResponseEntity<RestResponse<UserResponseDto>> createUser(@RequestBody @Valid UserRequestDTO userRequestDto, BindingResult bindingResult) {
        HttpStatus status;
        RestError restError = null;
        UserResponseDto responseDto = null;
        var responseEntity = errorHelper.checkErrors2(bindingResult);
        if (responseEntity == null) {
            status = HttpStatus.OK;
            responseDto = mapper.toResponseDto(userService.create(mapper.toModel(userRequestDto)));
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, responseEntity);
        }

        RestResponse<UserResponseDto> response = new RestResponse<>(restError, responseDto);
        return new ResponseEntity<>(response, status);
    }

    @PutMapping
    @TrackExecutionTime
    public ResponseEntity<Object> updateUser(Principal principal, @Valid UserRequestUpdateDTO crudDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(userService.update(principalMapper.toModel(principal), mapper2.toModel(crudDTO)));
        }
        return responseEntity;
    }

    @DeleteMapping
    @TrackExecutionTime
    public ResponseEntity<Void> deleteUser(Principal principal) {
        return ResponseEntity.ok(userService.delete(principalMapper.toModel(principal)));
    }

}