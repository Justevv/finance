package com.manager.user.administrator.controller;

import com.manager.user.administrator.model.RoleModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/role/{roleId}/permission")
@Slf4j
@RequiredArgsConstructor
public class PermissionsRole {
    private final RoleModel roleModel;

    @PostMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> addPermissions(@PathVariable("roleId") RoleEntity role, @RequestParam List<String> permissionIds) {
        var responseDTO = roleModel.addPermission(role, permissionIds);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public ResponseEntity<Object> deletePermissions(@PathVariable("roleId") RoleEntity role, @RequestParam List<String> permissionIds) {
        var responseDTO = roleModel.deletePermission(role, permissionIds);
        return ResponseEntity.ok(responseDTO);
    }

}
