package com.manager.user.domain.service;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
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
    private final UserSpringDataRepository userRepository;

    @Override
    @TrackExecutionTime
    public UserDetails loadUserByUsername(String username) {
        log.debug("Input username is {}", username);
        var userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        log.debug("Current user is {}", userEntity);
        return userEntity;
    }
}
