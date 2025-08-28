package com.manager.finance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "phone_verification")
@Data
@NoArgsConstructor
public class PhoneVerificationEntity implements Serializable {
    @Id
    private UUID id;
    @ToString.Exclude
    private String code;
    private LocalDateTime expireTime;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;
    private boolean isSent;

    public PhoneVerificationEntity(String code, int expireInSeconds) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireInSeconds);
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
