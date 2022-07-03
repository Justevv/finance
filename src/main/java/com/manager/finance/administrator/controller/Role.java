package com.manager.finance.administrator.controller;


import com.manager.finance.administrator.dto.RoleDTO;
import com.manager.finance.administrator.model.RoleModel;
import com.manager.finance.controller.Utils;
import com.manager.finance.entity.RoleEntity;
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
@RequestMapping("/v1/admin/role")
@Slf4j
public class Role {
    private static final String ROLE_LOG_NAME = "role";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(ROLE_LOG_NAME);
    @Autowired
    private RoleModel roleModel;

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
        ResponseEntity<Object> responseEntity;

        if (!bindingResult.hasErrors()) {
            var responseDTO = roleModel.create(role);
            responseEntity = ResponseEntity.ok(responseDTO);
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> updateRole(@PathVariable("id") RoleEntity role, @Valid RoleDTO roleDTO,
                                             BindingResult bindingResult) {
        ResponseEntity<Object> responseEntity;
        if (!bindingResult.hasErrors()) {
            responseEntity = ResponseEntity.ok(roleModel.update(role, roleDTO));
        } else {
            Map<String, String> errors = Utils.getErrors(bindingResult);
            log.debug(crudLogConstants.getErrorsAdded(), errors);
            responseEntity = new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") RoleEntity role) {
        return ResponseEntity.ok(roleModel.delete(role));
    }

}

