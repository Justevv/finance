package com.manager.finance.converter;

import com.manager.finance.entity.PermissionEntity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PermissionConverter implements AttributeConverter<PermissionEntity, String> {

    @Override
    public String convertToDatabaseColumn(PermissionEntity permission) {
        if (permission == null) {
            return null;
        }
        return permission.getPermission();
    }

    @Override
    public PermissionEntity convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(PermissionEntity.values())
                .filter(c -> c.getPermission().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

