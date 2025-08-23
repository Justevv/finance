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
@Table(name = "verification")
@Data
@NoArgsConstructor
public class VerificationEntity implements Serializable {
    @Id
    private UUID guid;
    @ToString.Exclude
    private String code;
    private LocalDateTime expireTime;
    private VerificationType type;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    public VerificationEntity(String code, int expireInSeconds, VerificationType type) {
        this.guid = UUID.randomUUID();
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireInSeconds);
        this.type = type;
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
