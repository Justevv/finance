package com.manager.user.controller;

import com.manager.user.dto.PasswordUpdateDTO;
import com.manager.user.dto.UserUpdateDTO;
import com.manager.finance.helper.ErrorHelper;
import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.service.verification.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/user/password")
@Slf4j
@RequiredArgsConstructor
public class Password {
    private final PasswordService passwordService;
    private final ErrorHelper errorHelper;

    @PostMapping("/forget")
    @TrackExecutionTime
    public ResponseEntity<Object> forgetPassword(@Valid UserUpdateDTO userDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            var response = Map.of("Token created", passwordService.createPasswordResetToken(userDTO.getEmail()));
            responseEntity = ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    @PostMapping("/reset")
    @TrackExecutionTime
    public ResponseEntity<Object> resetPassword(String token, @Valid PasswordUpdateDTO passwordUpdateDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            var isPasswordUpdated = passwordService.validatePasswordResetToken(token, passwordUpdateDTO);
            var response = Map.of("Password updated", isPasswordUpdated);
            responseEntity = ResponseEntity.ok(response);
        }
        return responseEntity;
    }

}
