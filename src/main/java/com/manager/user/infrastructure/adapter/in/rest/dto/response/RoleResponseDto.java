package com.manager.user.infrastructure.adapter.in.rest.dto.response;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import lombok.Builder;

import java.util.Collection;
import java.util.UUID;

@Builder
public record RoleResponseDto(
        UUID id,
        String name,
        Collection<PermissionEntity> permissions
) {
}
