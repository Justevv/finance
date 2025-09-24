package com.manager.user.infrastructure.adapter.in.rest.controller.user;

import com.manager.finance.infrastructure.adapter.in.rest.error.ErrorHelper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.in.PasswordUseCase;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.PasswordUpdateDTO;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserUpdateRequestDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.RestError;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.RestResponse;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserResponseDto;
import com.manager.user.infrastructure.adapter.in.rest.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/password")
@Slf4j
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordUseCase passwordUseCase;
    private final ErrorHelper errorHelper;
    private final DtoMapper<UserUpdateRequestDto, UserResponseDto, UserModel> mapper;

    @PostMapping("/forget")
    @TrackExecutionTime
    public ResponseEntity<RestResponse<String>> forgetPassword(@RequestBody @Valid UserUpdateRequestDto userDTO, BindingResult bindingResult) {
        HttpStatus status;
        RestError restError = null;
        String response = null;
        var errors = errorHelper.checkErrors2(bindingResult);
        if (errors == null) {
            passwordUseCase.createPasswordResetToken(mapper.toModel(userDTO));
            status = HttpStatus.OK;
            response = "Token created";
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, errors);
        }

        RestResponse<String> restResponse = new RestResponse<>(restError, response);
        return new ResponseEntity<>(restResponse, status);
    }

    @PostMapping("/reset")
    @TrackExecutionTime
    public ResponseEntity<RestResponse<String>> resetPassword(@RequestBody @Valid PasswordUpdateDTO passwordUpdateDTO, BindingResult bindingResult) {
        HttpStatus status;
        RestError restError = null;
        String response = null;
        var errors = errorHelper.checkErrors2(bindingResult);
        if (errors == null) {
            passwordUseCase.validatePasswordResetToken(passwordUpdateDTO);
            status = HttpStatus.OK;
            response = "Password updated";
        } else {
            status = HttpStatus.BAD_REQUEST;
            restError = new RestError(null, errors);
        }

        RestResponse<String> restResponse = new RestResponse<>(restError, response);
        return new ResponseEntity<>(restResponse, status);
    }

}
