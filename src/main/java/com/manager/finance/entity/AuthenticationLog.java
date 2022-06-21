package com.manager.finance.entity;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "authentication_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String ipAddress;
    private LocalDateTime dateTime;
    private UserAgent userAgent;

}
