package com.manager.finance.helper.converter;

import com.manager.finance.entity.PermissionEntity;
import com.manager.finance.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

@TestConfiguration
public class PermissionIdConverter implements Converter<String, PermissionEntity> {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public PermissionEntity convert(String source) {
        return permissionRepository.findById(Long.parseLong(source)).orElseThrow();
    }
}
