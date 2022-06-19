package com.manager.finance.service;

import com.manager.finance.entity.UserEntity;
import com.manager.finance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("Input username is {}", username);
        var userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        log.debug("Current user is {}", userEntity);
        return userEntity;
    }
}
