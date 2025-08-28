package com.manager.finance.helper.prepare;

import com.manager.finance.entity.PermissionEntity;
import com.manager.finance.entity.RoleEntity;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@TestConfiguration
public class RolePrepareHelper {
    public RoleEntity createRole() {
        var role = new RoleEntity();
        Set<PermissionEntity> permissions = new HashSet<>(List.of(PermissionEntity.ALL_READ));
        role.setId(UUID.randomUUID());
        role.setName("ROLE_USER");
        role.setPermissions(permissions);
        return role;
    }

}
