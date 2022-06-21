package com.manager.finance.model;

import com.manager.finance.event.AuthenticationEvent;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.security.AuthenticationRequestDTO;
import com.manager.finance.security.JwtProvider;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationModel {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public String authenticate(AuthenticationRequestDTO authentication) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentication.getUsername(), authentication.getPassword()));
        var user = userRepository.findByUsername(authentication.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        return jwtProvider.createToken(authentication.getUsername(), user.getRoles());
    }

    public void saveAuthenticateLog(UserAgent userAgent, String remoteAddr, String username) {
        eventPublisher.publishEvent(new AuthenticationEvent(userAgent, remoteAddr, username));
    }
}