package com.manager.finance.helper.converter;

import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

@TestConfiguration
public class UserIdConverter implements Converter<String, UserEntity> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity convert(String source) {
        return userRepository.findById(UUID.fromString(source)).orElseThrow();
    }
}
