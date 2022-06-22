package com.manager.finance.listener;

import com.manager.finance.entity.AuthenticationLog;
import com.manager.finance.event.AuthenticationEvent;
import com.manager.finance.repository.AuthenticationLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@EnableAsync
@Component
@Slf4j
public class AuthenticationListener {
    @Autowired
    private AuthenticationLogRepository authenticationLogRepository;

    @Async
    @EventListener
    public void onApplicationEvent(AuthenticationEvent event) {
        log.debug("Event start {}", event);
        var authenticationLog = AuthenticationLog.builder()
                .dateTime(LocalDateTime.now())
                .ipAddress(event.getRemoteAddr())
                .userAgent(event.getUserAgent())
                .username(event.getUsername())
                .build();
        authenticationLogRepository.save(authenticationLog);
    }
}
