package com.manager.user.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record PasswordResetTokenModel(
        UUID id,
        String token,
        UserModel user,
        LocalDateTime expireTime,
        ProcessStatus status
) implements Model {

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
