package com.manager.user.event;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuthenticationEvent extends ApplicationEvent {
    private final UserAgent userAgent;
    private final String remoteAddr;
    private final String username;

    public AuthenticationEvent(UserAgent userAgent, String remoteAddr, String username) {
        super(userAgent);
        this.userAgent = userAgent;
        this.remoteAddr = remoteAddr;
        this.username = username;
    }
}