package com.manager.finance.entity;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "authentication_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authentication_log_generator")
    @SequenceGenerator(name = "authentication_log_generator", sequenceName = "authentication_log_seq", allocationSize = 1)
    private Long id;
    private String username;
    private String ipAddress;
    private LocalDateTime dateTime;
    private UserAgent userAgent;

}
