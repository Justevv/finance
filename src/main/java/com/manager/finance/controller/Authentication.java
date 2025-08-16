package com.manager.finance.controller;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.service.AuthenticationService;
import com.manager.finance.security.AuthenticationRequestDTO;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("v1/auth")
@Slf4j
@RequiredArgsConstructor
public class Authentication {
    private static final String INVALID_USERNAME_PASSWORD = "Invalid username/password";
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @TrackExecutionTime
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequestDTO authenticationDTO, HttpServletRequest request) {
        log.debug("User {} tries to authenticate", authenticationDTO);
        var userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        try {
            var authentication = authenticationService.authenticate(userAgent, request.getRemoteAddr(), authenticationDTO);
            log.info("User {} was successfully authenticated", authenticationDTO);
            return ResponseEntity.ok(authentication);
        } catch (AuthenticationException e) {
            log.warn(INVALID_USERNAME_PASSWORD + " for user {}", authenticationDTO);
            var error = Map.of("error", INVALID_USERNAME_PASSWORD);
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    @TrackExecutionTime
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        var securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}

