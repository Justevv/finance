package com.manager.finance.controller;

import com.manager.finance.dto.PasswordUpdateDTO;
import com.manager.finance.dto.UserUpdateDTO;
import com.manager.finance.helper.ErrorHelper;
import com.manager.finance.model.PasswordModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/user/password")
@Slf4j
public class Password {
    @Autowired
    private PasswordModel passwordModel;
    @Autowired
    private ErrorHelper errorHelper;

    @PostMapping("/forget")
    public ResponseEntity<Object> forgetPassword(@Valid UserUpdateDTO userDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            var response = Map.of("Token created", passwordModel.createPasswordResetToken(userDTO.getEmail()));
            responseEntity = ResponseEntity.ok(response);
        }
        return responseEntity;
    }

    @PostMapping("/reset")
    public ResponseEntity<Object> resetPassword(String token, @Valid PasswordUpdateDTO passwordUpdateDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            var isPasswordUpdated = passwordModel.validatePasswordResetToken(token, passwordUpdateDTO);
            var response = Map.of("Password updated", isPasswordUpdated);
            responseEntity = ResponseEntity.ok(response);
        }
        return responseEntity;
    }

}
