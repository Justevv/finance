package com.manager.finance.helper.prepare;

import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.util.Set;
import java.util.UUID;

@TestConfiguration
@Import({RolePrepareHelper.class})
public class UserPrepareHelper {
    @Autowired
    private RolePrepareHelper rolePrepareHelper;

    public UserEntity createUser() {
        var userEntity = new UserEntity();
        userEntity.setId(UUID.fromString("3a2830b2-6d06-4964-bba5-e90a29d0fcd0"));
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

