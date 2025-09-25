package com.manager.user.infrastructure.adapter.in.rest.dto.response;

import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.Builder;

import java.util.Collection;
import java.util.UUID;

@Builder
public record UserAdminResponseDto(
        UUID id,
        String username,
        String email,
        String phone,
        boolean isPhoneConfirmed,
        boolean isEmailConfirmed,
        Collection<RoleEntity> roles
) {

}