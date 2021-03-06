package com.manager.finance.controller;

import com.manager.finance.model.AuthenticationModel;
import com.manager.finance.security.AuthenticationRequestDTO;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("v1/auth")
@Slf4j
public class Authentication {
    private static final String INVALID_USERNAME_PASSWORD = "Invalid username/password";
    @Autowired
    private AuthenticationModel authenticationModel;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequestDTO authenticationDTO, HttpServletRequest request) {
        log.debug("User {} tries to authenticate", authenticationDTO);
        var userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        try {
            var authentication = authenticationModel.authenticate(userAgent, request.getRemoteAddr(), authenticationDTO);
            log.info("User {} was successfully authenticated", authenticationDTO);
            return ResponseEntity.ok(authentication);
        } catch (AuthenticationException e) {
            log.warn(INVALID_USERNAME_PASSWORD + " for user {}", authenticationDTO);
            var error = Map.of("error", INVALID_USERNAME_PASSWORD);
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        var securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}

