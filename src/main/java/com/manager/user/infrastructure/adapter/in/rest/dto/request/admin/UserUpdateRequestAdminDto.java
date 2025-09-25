package com.manager.user.infrastructure.adapter.in.rest.dto.request.admin;

import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserUpdateRequestDto;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.Builder;

import java.util.Collection;

@Builder
public record UserUpdateRequestAdminDto(
        UserUpdateRequestDto user,
        Boolean isPhoneConfirmed,
        Boolean isEmailConfirmed,
        Collection<RoleEntity> roles
) {

}
