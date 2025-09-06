package com.manager.finance.infrastructure.adapter.in.rest.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record Error(
        String text,
        Map<String, String> fields
) {
}

