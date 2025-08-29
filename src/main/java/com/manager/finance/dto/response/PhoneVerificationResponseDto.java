package com.manager.finance.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PhoneVerificationResponseDto(
        UUID id,
        String phone,
        String code
) {

}

