package com.manager.user.infrastructure.adapter.out.persistence.entity;

import com.manager.user.domain.model.ProcessStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetTokenEntity {
    @Id
    private UUID id;
    @ToString.Exclude
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity user;
    private LocalDateTime expireTime;
    private ProcessStatus status;

}
