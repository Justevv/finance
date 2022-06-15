package com.manager.finance.controller;

import com.manager.finance.model.AuthenticationModel;
import com.manager.finance.security.AuthenticationRequestDTO;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1/auth")
@Slf4j
public class Authentication {
    @Autowired
    AuthenticationModel authenticationModel;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequestDTO authentication) {
        try {
            Map<String, String> response = new HashMap<>();
            response.put("username", authentication.getUsername());
            response.put("token", authenticationModel.authenticate(authentication));
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            log.warn("Invalid username/password {}", authentication.getUsername());
            return new ResponseEntity<>("Invalid username/password", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        var securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}

