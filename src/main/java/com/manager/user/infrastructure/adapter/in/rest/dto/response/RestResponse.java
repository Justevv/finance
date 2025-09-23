package com.manager.user.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

@Builder
public record RestResponse<R>(
        RestError error,
        R payload
) {
}

