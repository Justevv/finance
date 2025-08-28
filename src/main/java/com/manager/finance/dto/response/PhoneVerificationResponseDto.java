package com.manager.finance.dto.response;

import lombok.Builder;

@Builder
public record PhoneVerificationResponseDto(
        String phone,
        String code
) {

}

