package com.manager.finance.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

@Builder
public record ExpenseResponse<R>(
        Error error,
        R payload
) {
}

