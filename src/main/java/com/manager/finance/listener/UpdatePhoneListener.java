package com.manager.finance.listener;

import com.manager.finance.service.ConfirmationService;
import com.manager.finance.event.OnPhoneUpdateCompleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
@Slf4j
public class UpdatePhoneListener {
    @Autowired
    private ConfirmationService confirmationService;

    @Async
    @EventListener
    public void onApplicationEvent(OnPhoneUpdateCompleteEvent event) {
        log.debug("Event start {}", event);
        confirmationService.sendConfirmPhone(event.getVerification());
    }
}
