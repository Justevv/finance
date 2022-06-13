package com.manager.finance.controller;


import com.manager.finance.config.LogConstants;
import com.manager.finance.dto.UserDTO;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.exception.UserAlreadyExistException;
import com.manager.finance.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class User {
    private static final String USER_LOG_NAME = "user";
    private final LogConstants logConstants = new LogConstants(USER_LOG_NAME);
    @Autowired
    private UserModel userModel;

    @GetMapping
    public List<UserEntity> getUsers() {
        log.debug("getUsers");
        return userModel.getUsers();
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid UserDTO userDTO, BindingResult bindingResult) throws UserAlreadyExistException {
        log.debug(logConstants.getInputDataNew(), userDTO);
        ResponseEntity<Object> responseEntity;

        if (!bindingResult.hasErrors()) {
            var user = userModel.create(userDTO);
            log.debug(logConstants.getSaveToDatabase(), user);
            responseEntity = ResponseEntity.ok(null);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getSavedResponse(), responseEntity);
        return responseEntity;
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") UserEntity user, UserDTO userDTO,
                                             BindingResult bindingResult) throws UserAlreadyExistException {
        log.debug(logConstants.getInputDataToChange(), userDTO, user);
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(userModel.update(user, userDTO));
            log.debug(logConstants.getSaveToDatabase(), userDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(logConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(logConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") UserEntity user) {
        log.debug(logConstants.getInputDataForDelete(), user);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(userModel.delete(user));
        log.debug(logConstants.getDeletedResponse(), responseEntity);
        return responseEntity;
    }

}

