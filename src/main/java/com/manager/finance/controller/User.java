package com.manager.finance.controller;


import com.manager.finance.dto.UserDTO;
import com.manager.finance.dto.UserUpdateDTO;
import com.manager.finance.dto.response.UserResponseDTO;
import com.manager.finance.helper.ErrorHelper;
import com.manager.finance.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/v1/user")
@Slf4j
public class User {
    @Autowired
    private UserModel userModel;
    @Autowired
    private ErrorHelper errorHelper;

    @GetMapping
    public UserResponseDTO getUser(Principal principal) {
        return userModel.getUser(principal);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid UserDTO userDTO, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            var responseDTO = userModel.createAndGetDTO(userDTO);
            responseEntity = ResponseEntity.ok(responseDTO);
        }
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<Object> updateUser(Principal principal, @Valid UserUpdateDTO crudDTO, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(userModel.update(principal, crudDTO));
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(Principal principal) {
        return ResponseEntity.ok(userModel.delete(principal));
    }

}