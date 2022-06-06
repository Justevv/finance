package com.manager.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class Log implements HandlerInterceptor {
    public static final String SESSION_ID_KEY = "sessionId";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        CloseableThreadContext.put(SESSION_ID_KEY,
                RequestContextHolder.currentRequestAttributes().getSessionId());
        return true;
    }
}
