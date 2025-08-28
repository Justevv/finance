package com.manager.finance.dto.response;

import lombok.Builder;

@Builder
public record EmailVerificationResponseDto(
        String email,
        String code
) {

}

