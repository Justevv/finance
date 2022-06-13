package com.manager.finance.model;

import com.manager.finance.repository.UserRepository;
import com.manager.finance.security.AuthenticationRequestDTO;
import com.manager.finance.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationModel {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;

    public String authenticate(AuthenticationRequestDTO authentication) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentication.getUsername(), authentication.getPassword()));
        var user = userRepository.findByUsername(authentication.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        return jwtProvider.createToken(authentication.getUsername(), user.getRole());
    }
}