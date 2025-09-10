package com.manager.user.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDto(
        UUID id,
        String username,
        String email,
        String phone,
        boolean isPhoneConfirmed,
        boolean isEmailConfirmed
) {

}
