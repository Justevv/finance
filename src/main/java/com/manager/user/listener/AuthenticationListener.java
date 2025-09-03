package com.manager.user.listener;

import com.manager.user.entity.AuthenticationLog;
import com.manager.user.event.AuthenticationEvent;
import com.manager.user.repository.AuthenticationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@EnableAsync
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationListener {
    private final AuthenticationLogRepository authenticationLogRepository;

    @Async
    @EventListener
    public void onApplicationEvent(AuthenticationEvent event) {
        log.debug("Event start {}", event);
        var authenticationLog = AuthenticationLog.builder()
                .id(UUID.randomUUID())
                .dateTime(LocalDateTime.now())
                .ipAddress(event.getRemoteAddr())
                .userAgent(event.getUserAgent())
                .username(event.getUsername())
                .build();
        authenticationLogRepository.save(authenticationLog);
    }
}
