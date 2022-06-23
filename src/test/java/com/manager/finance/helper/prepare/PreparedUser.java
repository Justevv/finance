package com.manager.finance.helper.prepare;

import com.manager.finance.entity.Permission;
import com.manager.finance.entity.Role;
import com.manager.finance.entity.UserEntity;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Collection;
import java.util.List;

@TestConfiguration
public class PreparedUser {

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
        userEntity.setUsername("user");
        userEntity.setPhone("phone");
        userEntity.setPassword("$2a$04$kLf5hQQ8yshxEfcMk9etVupP2It5u889YM9KLVpuAnSEAvc3oDq.6");
        userEntity.setEmail("email@email.ru");
        userEntity.setEmailConfirmed(true);
        userEntity.setPhoneConfirmed(true);
        return userEntity;
    }
}

