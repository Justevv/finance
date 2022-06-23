package com.manager.finance.controller;

import com.manager.finance.dto.PasswordUpdateDTO;
import com.manager.finance.dto.UserUpdateDTO;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.model.PasswordModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/user/password")
@Slf4j
public class Password {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    @Autowired
    private PasswordModel passwordModel;

    @PostMapping("/forget")
    public ResponseEntity<Object> forgetPassword(@Valid UserUpdateDTO userDTO, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            var response = Map.of("Token created", passwordModel.createPasswordResetToken(userDTO.getEmail()));
            responseEntity = ResponseEntity.ok(response);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/reset")
    public ResponseEntity<Object> resetPassword(String token, @Valid PasswordUpdateDTO passwordUpdateDTO, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            var isPasswordUpdated = passwordModel.validatePasswordResetToken(token, passwordUpdateDTO);
            var response = Map.of("Password updated", isPasswordUpdated);
            responseEntity = ResponseEntity.ok(response);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

}
