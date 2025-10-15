package com.manager.user.domain.model;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record RoleModel(
        UUID id,
        String name,
        Set<PermissionEntity> permissions
) {
}
