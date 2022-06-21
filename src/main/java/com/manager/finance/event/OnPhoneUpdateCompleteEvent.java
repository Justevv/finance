package com.manager.finance.event;

import com.manager.finance.entity.VerificationEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnPhoneUpdateCompleteEvent extends ApplicationEvent {
    private final VerificationEntity verification;

    public OnPhoneUpdateCompleteEvent(VerificationEntity verification) {
        super(verification);
        this.verification = verification;
    }

}