package com.manager.finance.helper.prepare;

import com.manager.finance.entity.PermissionEntity;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class PermissionPrepareHelper {

    public PermissionEntity createPermission() {
        var permission = new PermissionEntity();
        permission.setId(1L);
        permission.setName("permission");
        return permission;
    }

}
