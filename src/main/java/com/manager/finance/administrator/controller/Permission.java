package com.manager.finance.administrator.controller;


import com.manager.finance.administrator.model.PermissionModel;
import com.manager.finance.entity.PermissionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/permission")
@Slf4j
public class Permission {
    @Autowired
    private PermissionModel permissionModel;

    @GetMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public List<PermissionEntity> getPermissions() {
        return permissionModel.getPermissions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public PermissionEntity getPermission(@PathVariable("id") PermissionEntity permission) {
        return permission;
    }

}

