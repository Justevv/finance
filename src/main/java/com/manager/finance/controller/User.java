package com.manager.finance.controller;


import com.manager.finance.dto.user.UserDTO;
import com.manager.finance.dto.user.UserResponseDTO;
import com.manager.finance.dto.user.UserUpdateDTO;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
@Slf4j
public class User {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    @Autowired
    private UserModel userModel;

    @GetMapping
    public UserResponseDTO getUser(Principal principal) {
        return userModel.getUser(principal);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid UserDTO userDTO, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;

        if (!bindingResult.hasErrors()) {
            var responseDTO = userModel.createAndGetDTO(userDTO);
            responseEntity = ResponseEntity.ok(responseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<Object> updateUser(Principal principal, @Valid UserUpdateDTO crudDTO, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(userModel.update(principal, crudDTO));
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(Principal principal) {
        return ResponseEntity.ok(userModel.delete(principal));
    }

}