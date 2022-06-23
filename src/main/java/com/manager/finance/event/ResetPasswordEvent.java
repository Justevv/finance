package com.manager.finance.event;

import com.manager.finance.entity.PasswordResetToken;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ResetPasswordEvent extends ApplicationEvent {
    private final PasswordResetToken passwordToken;

    public ResetPasswordEvent(PasswordResetToken passwordToken) {
        super(passwordToken);
        this.passwordToken = passwordToken;
    }

}
