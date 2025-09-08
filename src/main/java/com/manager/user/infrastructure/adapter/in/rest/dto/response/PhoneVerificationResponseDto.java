package com.manager.user.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PhoneVerificationResponseDto(
        UUID id,
        String phone,
        String code
) {

}

