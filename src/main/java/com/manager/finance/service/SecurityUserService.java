package com.manager.finance.service;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @TrackExecutionTime
    public UserDetails loadUserByUsername(String username) {
        log.debug("Input username is {}", username);
        var userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        log.debug("Current user is {}", userEntity);
        return userEntity;
    }
}
