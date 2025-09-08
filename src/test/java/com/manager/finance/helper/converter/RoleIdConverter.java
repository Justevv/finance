package com.manager.finance.helper.converter;

import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.manager.user.infrastructure.adapter.out.persistence.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

@TestConfiguration
public class RoleIdConverter implements Converter<String, RoleEntity> {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleEntity convert(String source) {
        return roleRepository.findById(UUID.fromString(source)).orElseThrow();
    }
}
