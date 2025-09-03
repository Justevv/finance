package com.manager.user.entity;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "authentication_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthenticationLog {
    @Id
    private UUID id;
    private String username;
    private String ipAddress;
    private LocalDateTime dateTime;
    private UserAgent userAgent;

}
