package com.manager.user.event;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PasswordResetTokenEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ResetPasswordEvent extends ApplicationEvent {
    private final PasswordResetTokenEntity passwordToken;

    public ResetPasswordEvent(PasswordResetTokenEntity passwordToken) {
        super(passwordToken);
        this.passwordToken = passwordToken;
    }

}
