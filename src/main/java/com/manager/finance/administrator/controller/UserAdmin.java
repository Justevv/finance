package com.manager.finance.administrator.controller;


import com.manager.finance.administrator.dto.user.UserAdminDTO;
import com.manager.finance.administrator.dto.user.UserAdminResponseDTO;
import com.manager.finance.administrator.dto.user.UserAdminUpdateDTO;
import com.manager.finance.administrator.model.UserAdminModel;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.helper.ErrorHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/admin/user")
@Slf4j
public class UserAdmin {
    @Autowired
    private UserAdminModel userAdminModel;
    @Autowired
    private ErrorHelper errorHelper;

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
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(userAdminModel.createAndGetDTO(userAdminDTO));
        }
        return responseEntity;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") UserEntity user, @Valid UserAdminUpdateDTO userUpdateDTO,
                                             BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(userAdminModel.update(user, userUpdateDTO));
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UserEntity user) {
        return ResponseEntity.ok(userAdminModel.delete(user));
    }

}

