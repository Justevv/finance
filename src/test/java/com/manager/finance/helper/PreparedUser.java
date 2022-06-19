package com.manager.finance.helper;

import com.manager.finance.entity.Permission;
import com.manager.finance.entity.Role;
import com.manager.finance.entity.UserEntity;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Collection;
import java.util.List;

@TestConfiguration
public class PreparedUser {

    public UserEntity createSuperAdmin() {
        var userEntity = createMainUser();
        Collection<Permission> permissions = List.of(new Permission("user:read"),
                new Permission("user:write"), new Permission("user:delete"), new Permission("all:read"),
                new Permission("all:write"), new Permission("user:delete"));
        var role = new Role();
        role.setName("ROLE_SUPER_ADMIN");
        role.setPermissions(permissions);
        userEntity.setRoles(List.of(role));
        return userEntity;
    }

    public UserEntity createAdmin() {
        var userEntity = createMainUser();
        Collection<Permission> permissions = List.of(new Permission("user:read"),
                new Permission("user:write"), new Permission("user:delete"));
        var role = new Role();
        role.setName("ROLE_ADMIN");
        role.setPermissions(permissions);
        userEntity.setRoles(List.of(role));
        return userEntity;
    }

    public UserEntity createUser() {
        var userEntity = createMainUser();
        Collection<Permission> permissions = List.of(new Permission("user"));
        var role = new Role();
        role.setName("ROLE_USER");
        role.setPermissions(permissions);
        userEntity.setRoles(List.of(role));
        return userEntity;
    }

    private UserEntity createMainUser() {
        var userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("any");
        userEntity.setPhone("phone");
        userEntity.setPassword("$2a$04$nJrdi9bRvGBTNYKf1BCQu.kMPTGLJzmZcaKLsUKput4gfzsWq8Uty");
        userEntity.setEmail("email");
        userEntity.setEmailConfirmed(true);
        userEntity.setPhoneConfirmed(true);
        return userEntity;
    }
}

