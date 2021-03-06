package com.manager.finance.helper.prepare;

import com.manager.finance.entity.PermissionEntity;
import com.manager.finance.entity.RoleEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.util.Set;

@TestConfiguration
@Import({RolePrepareHelper.class})
public class UserPrepareHelper {
    @Autowired
    private RolePrepareHelper rolePrepareHelper;

    public UserEntity createUser() {
        var userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("user");
        userEntity.setPhone("phone");
        userEntity.setPassword("$2a$04$kLf5hQQ8yshxEfcMk9etVupP2It5u889YM9KLVpuAnSEAvc3oDq.6");
        userEntity.setEmail("email@email.ru");
        userEntity.setEmailConfirmed(true);
        userEntity.setPhoneConfirmed(true);
        userEntity.setRoles(Set.of(rolePrepareHelper.createRole()));
        return userEntity;
    }
}

