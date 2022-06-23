package com.manager.finance.helper.prepare;

import com.manager.finance.entity.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.time.LocalDateTime;

@TestConfiguration
public class PreparedPasswordResetToken {
    @Autowired
    private PreparedUser preparedUser;

    public PasswordResetToken createPasswordResetToken() {
        var passwordResetToken = new PasswordResetToken();
        passwordResetToken.setId(1L);
        passwordResetToken.setToken("token");
        passwordResetToken.setUser(preparedUser.createUser());
        passwordResetToken.setExpireTime(LocalDateTime.MAX);
        return passwordResetToken;
    }
}

