package com.manager.finance.administrator.controller;

import com.manager.finance.administrator.model.RoleModel;
import com.manager.finance.entity.RoleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/role/{roleId}/permission")
@Slf4j
public class PermissionsRole {
    @Autowired
    private RoleModel roleModel;

    @PostMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> addPermissions(@PathVariable("roleId") RoleEntity role, @RequestParam List<Long> permissionIds) {
        var responseDTO = roleModel.addPermission(role, permissionIds);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> deletePermissions(@PathVariable("roleId") RoleEntity role, @RequestParam List<Long> permissionIds) {
        var responseDTO = roleModel.deletePermission(role, permissionIds);
        return ResponseEntity.ok(responseDTO);
    }

}
