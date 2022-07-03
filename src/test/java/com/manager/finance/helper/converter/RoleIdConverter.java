package com.manager.finance.helper.converter;

import com.manager.finance.entity.RoleEntity;
import com.manager.finance.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

@TestConfiguration
public class RoleIdConverter implements Converter<String, RoleEntity> {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleEntity convert(String source) {
        return roleRepository.findById(Long.parseLong(source)).orElseThrow();
    }
}
