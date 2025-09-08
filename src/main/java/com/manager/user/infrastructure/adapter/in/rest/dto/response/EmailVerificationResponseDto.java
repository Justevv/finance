package com.manager.user.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EmailVerificationResponseDto(
        UUID id,
        String email,
        String code
) {

}

