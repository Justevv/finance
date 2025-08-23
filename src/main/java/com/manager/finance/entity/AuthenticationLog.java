package com.manager.finance.entity;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "authentication_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationLog {
    @Id
    private UUID guid;
    private String username;
    private String ipAddress;
    private LocalDateTime dateTime;
    private UserAgent userAgent;

}
