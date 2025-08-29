package com.manager.finance.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EmailVerificationResponseDto(
        UUID id,
        String email,
        String code
) {

}

