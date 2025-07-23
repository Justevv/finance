package com.manager.finance.entity;

import lombok.Data;
import lombok.ToString;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
@Data
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_token_generator")
    @SequenceGenerator(name = "password_reset_token_generator", sequenceName = "password_reset_token_seq", allocationSize = 1)
    private Long id;
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
