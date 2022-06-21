package com.manager.finance.listener;

import com.manager.finance.ConfirmationHelper;
import com.manager.finance.event.OnEmailUpdateCompleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
@Slf4j
public class UpdateEmailListener {
    @Autowired
    private ConfirmationHelper confirmationHelper;

    @Async
    @EventListener
    public void onApplicationEvent(OnEmailUpdateCompleteEvent event) {
        log.debug("Event start {}", event);
        confirmationHelper.sendConfirmEmail(event.getVerification());
    }
}
