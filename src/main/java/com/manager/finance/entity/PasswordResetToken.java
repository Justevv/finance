package com.manager.finance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
public class PasswordResetToken {

    @Id
    private UUID id;
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
