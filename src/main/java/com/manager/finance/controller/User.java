package com.manager.finance.controller;


import com.manager.finance.config.CrudLogConstants;
import com.manager.finance.dto.CrudResponseDTO;
import com.manager.finance.dto.UserDTO;
import com.manager.finance.dto.UserUpdateDTO;
import com.manager.finance.entity.UserEntity;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
@Slf4j
public class User extends CrudApiResponse<UserModel, UserEntity> {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    @Autowired
    private UserModel userModel;

    public User(UserModel userModel) {
        super(userModel, USER_LOG_NAME);
        this.userModel = userModel;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public List<UserEntity> getUsers() {
        return userModel.getAll();
    }

    @GetMapping("/myself")
    public UserEntity getUser(Principal principal) {
        return userModel.getUser(principal);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid UserDTO userDTO, BindingResult bindingResult) throws Exception {
        log.debug(crudLogConstants.getInputDataNew(), userDTO);
        ResponseEntity<Object> responseEntity;

        if (!bindingResult.hasErrors()) {
            CrudResponseDTO responseDTO = userModel.create(userDTO);
            log.debug(crudLogConstants.getSaveToDatabase(), responseDTO);
            responseEntity = ResponseEntity.ok(responseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorAdd(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(crudLogConstants.getSavedResponse(), responseEntity);
        return responseEntity;
//        return create(userDTO, bindingResult);
    }

    @PutMapping("/myself")
    public ResponseEntity<Object> updateUser(UserUpdateDTO crudDTO, Principal principal,
                                             BindingResult bindingResult) throws Exception {
        log.debug(crudLogConstants.getInputDataToChange(), principal, crudDTO);
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(userModel.update(principal, crudDTO));
            log.debug(crudLogConstants.getSaveToDatabase(), crudDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorChange(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        log.debug(crudLogConstants.getUpdatedResponse(), responseEntity);
        return responseEntity;
//        return update(user, userDTO, bindingResult);
    }

    @DeleteMapping("/myself")
    public ResponseEntity<Void> deleteUser(Principal principal) {
        log.debug(crudLogConstants.getInputDataForDelete(), principal);
        var responseEntity = ResponseEntity.ok(userModel.delete(principal));
        log.debug(crudLogConstants.getDeletedResponse(), responseEntity);
        return responseEntity;
//        return delete(user);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('users:update')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") UserEntity user, UserUpdateDTO crudDTO,
                                             BindingResult bindingResult) {
        return update(user, crudDTO, bindingResult);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('users:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UserEntity user) {
        return delete(user);
    }

}