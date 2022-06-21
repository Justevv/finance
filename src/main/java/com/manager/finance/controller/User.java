package com.manager.finance.controller;


import com.manager.finance.dto.CrudResponseDTO;
import com.manager.finance.dto.UserDTO;
import com.manager.finance.dto.UserResponseDTO;
import com.manager.finance.dto.UserUpdateDTO;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.exception.UserAlreadyExistException;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
@Slf4j
public class User extends CrudApiResponse<UserModel, UserEntity> {
    private static final String USER_LOG_NAME = "user";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_LOG_NAME);
    private final UserModel userModel;

    public User(UserModel userModel) {
        super(userModel, USER_LOG_NAME);
        this.userModel = userModel;
    }

    @GetMapping({"", "/all"})
    @PreAuthorize("hasAuthority('user:read')")
    public List<UserEntity> getUsers() {
        return userModel.getUsersAllInfo();
    }

    @GetMapping("/me")
    public UserResponseDTO getUser(Principal principal) {
        return userModel.getUser(principal);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid UserDTO userDTO, BindingResult bindingResult) throws Exception {
        ResponseEntity<Object> responseEntity;

        if (!bindingResult.hasErrors()) {
            CrudResponseDTO responseDTO = userModel.create(userDTO);
            responseEntity = ResponseEntity.ok(responseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
//        return create(userDTO, bindingResult);
    }

    @PutMapping("/me")
    public ResponseEntity<Object> updateUser(Principal principal, @Valid UserUpdateDTO crudDTO,
                                             BindingResult bindingResult) throws Exception {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(userModel.update(principal, crudDTO));
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
//        return update(user, userDTO, bindingResult);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(Principal principal) {
        return ResponseEntity.ok(userModel.delete(principal));
//        return delete(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") UserEntity user, @Valid UserUpdateDTO crudDTO,
                                             BindingResult bindingResult) throws UserAlreadyExistException {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(userModel.update(user, crudDTO));
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UserEntity user) {
        return delete(user);
    }

}