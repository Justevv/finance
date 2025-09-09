package com.manager.user.domain.model;

import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record VerificationModel(
        UUID id,
        @ToString.Exclude
        String code,
        LocalDateTime expireTime,
        UserModel user,
        boolean isSent
) implements Model {

        public boolean isExpire() {
                return LocalDateTime.now().isAfter(expireTime);
        }
}
