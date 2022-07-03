package com.manager.finance.administrator.controller;


import com.manager.finance.administrator.dto.user.UserAdminDTO;
import com.manager.finance.administrator.dto.user.UserAdminResponseDTO;
import com.manager.finance.administrator.dto.user.UserAdminUpdateDTO;
import com.manager.finance.administrator.model.UserAdminModel;
import com.manager.finance.controller.Utils;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.log.CrudLogConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/admin/user")
@Slf4j
public class UserAdmin {
    private static final String USER_UNDER_ADMIN_LOG_NAME = "user under admin";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(USER_UNDER_ADMIN_LOG_NAME);
    @Autowired
    private UserAdminModel userAdminModel;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public List<UserAdminResponseDTO> getUsers() {
        return userAdminModel.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public UserAdminResponseDTO getUser(@PathVariable("id") UserEntity user) {
        return userAdminModel.get(user);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Object> createUser(@Valid UserAdminDTO userAdminDTO, BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;

        if (!bindingResult.hasErrors()) {
            var responseDTO = userAdminModel.createAndGetDTO(userAdminDTO);
            responseEntity = ResponseEntity.ok(responseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") UserEntity user, @Valid UserAdminUpdateDTO userUpdateDTO,
                                             BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(userAdminModel.update(user, userUpdateDTO));
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
        return ResponseEntity.ok(userAdminModel.delete(user));
    }

}

