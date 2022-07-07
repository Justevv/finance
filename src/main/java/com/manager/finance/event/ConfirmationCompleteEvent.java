package com.manager.finance.event;

import com.manager.finance.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ConfirmationCompleteEvent extends ApplicationEvent {
    private UserEntity user;

    public ConfirmationCompleteEvent(UserEntity user) {
        super(user);
        this.user = user;
    }

}
