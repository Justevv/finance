package com.manager.finance.helper.converter;

import com.manager.finance.entity.UserEntity;
import com.manager.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

@TestConfiguration
public class UserIdConverter implements Converter<String, UserEntity> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity convert(String source) {
        return userRepository.findById(Long.parseLong(source)).orElseThrow();
    }
}
