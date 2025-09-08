package com.manager.user.infrastructure.adapter.in.rest.controller.admin;


import com.manager.user.domain.service.admin.PermissionService;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/permission")
@Slf4j
@RequiredArgsConstructor
public class Permission {
    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('role:crud')")
    public List<PermissionEntity> getPermissions() {
        return permissionService.getPermissions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:crud')")
    public PermissionEntity getPermission(@PathVariable("id") PermissionEntity permission) {
        return permission;
    }

}

