package com.manager.finance.helper.prepare;

import com.manager.finance.entity.PermissionEntity;
import com.manager.finance.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@TestConfiguration
@Import({PermissionPrepareHelper.class})
public class RolePrepareHelper {
    @Autowired
    private PermissionPrepareHelper permissionPrepareHelper;

    public RoleEntity createRole() {
        var role = new RoleEntity();
        Set<PermissionEntity> permissions = new HashSet<>(List.of(permissionPrepareHelper.createPermission()));
        role.setId(1L);
        role.setName("ROLE_USER");
        role.setPermissions(permissions);
        return role;
    }

}
