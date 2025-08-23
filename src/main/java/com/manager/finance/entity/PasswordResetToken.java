package com.manager.finance.entity;

import lombok.Data;
import lombok.ToString;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token")
@Data
public class PasswordResetToken {

    @Id
    private UUID guid;
    @ToString.Exclude
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity user;
    private LocalDateTime expireTime;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, UserEntity user, int expireInSeconds) {
        this.token = token;
        this.user = user;
        this.expireTime = LocalDateTime.now().plusSeconds(expireInSeconds);
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
