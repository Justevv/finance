package com.manager.finance.administrator.controller;


import com.manager.finance.administrator.dto.RoleDTO;
import com.manager.finance.administrator.model.RoleModel;
import com.manager.finance.entity.RoleEntity;
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
@RequestMapping("/v1/admin/role")
@Slf4j
public class Role {
    @Autowired
    private RoleModel roleModel;
    @Autowired
    private ErrorHelper errorHelper;

    @GetMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public List<RoleEntity> getRoles() {
        return roleModel.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public RoleEntity getRole(@PathVariable("id") RoleEntity role) {
        return role;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> createRole(@Valid RoleDTO role, BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(roleModel.create(role));
        }
        return responseEntity;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> updateRole(@PathVariable("id") RoleEntity role, @Valid RoleDTO roleDTO,
                                             BindingResult bindingResult) {
        var responseEntity = errorHelper.checkErrors(bindingResult);
        if (responseEntity == null) {
            responseEntity = ResponseEntity.ok(roleModel.update(role, roleDTO));
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") RoleEntity role) {
        return ResponseEntity.ok(roleModel.delete(role));
    }

}

