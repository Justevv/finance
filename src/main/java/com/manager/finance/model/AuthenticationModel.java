package com.manager.finance.model;

import com.manager.finance.event.AuthenticationEvent;
import com.manager.finance.exception.UserIpAddressWasBlockedException;
import com.manager.finance.repository.AuthenticationLogRepository;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.security.AuthenticationRequestDTO;
import com.manager.finance.security.JwtProvider;
import com.manager.finance.service.LoginAttemptService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.manager.finance.constant.Constant.USER_DOES_NOT_EXISTS;

@Service
@Slf4j
public class AuthenticationModel {
    @Value("${authentication.blockPeriod}")
    private int blockPeriod;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private AuthenticationLogRepository authenticationLogRepository;
    @Autowired
    private LoginAttemptService loginAttemptService;

    public Map<String, String> authenticate(UserAgent userAgent, String remoteAddr, AuthenticationRequestDTO authentication) {
        publishAuthenticateEvent(userAgent, remoteAddr, authentication.getUsername());
        return authenticate(remoteAddr, authentication);
    }

    private Map<String, String> authenticate(String remoteAddr, AuthenticationRequestDTO authentication) {
        var username = authentication.getUsername();
        if (loginAttemptService.isBlocked(remoteAddr)) {
            log.warn("Too many count of failed login attempts. User with ip {} was blocked until {}",
                    remoteAddr, LocalDateTime.now().plusSeconds(blockPeriod));
            throw new UserIpAddressWasBlockedException("Too many count of failed login attempts");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                authentication.getPassword()));
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        var token = jwtProvider.createToken(username, user.getRoles());
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);
        return response;
    }

    private void publishAuthenticateEvent(UserAgent userAgent, String remoteAddr, String username) {
        eventPublisher.publishEvent(new AuthenticationEvent(userAgent, remoteAddr, username));
    }
}