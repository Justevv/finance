package com.manager.user.infrastructure.adapter.in.rest.dto.request;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import lombok.Builder;

import java.util.Set;

@Builder
public record RoleRequestDto(
        String name,
        Set<PermissionEntity> permissions
) {

}
