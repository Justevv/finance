package com.manager.finance.helper.prepare;

import com.manager.finance.entity.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

@TestConfiguration
public class PasswordResetTokenPrepareHelper {
    @Autowired
    private UserPrepareHelper userPrepareHelper;

    public PasswordResetToken createPasswordResetToken() {
        var passwordResetToken = new PasswordResetToken();
        passwordResetToken.setGuid(UUID.randomUUID());
        passwordResetToken.setToken("token");
        passwordResetToken.setUser(userPrepareHelper.createUser());
        passwordResetToken.setExpireTime(LocalDateTime.MAX);
        return passwordResetToken;
    }
}

