package com.manager.user.security;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.UserService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long validityInSeconds;
    @Value("${jwt.header}")
    private String authorizationHeader;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @TrackExecutionTime
    public String createToken(String username, UserModel user) {
        var claims = Jwts.claims().setSubject(username);
        claims.setId(user.id().toString());
        claims.put("roles", user.roles());
        var currentTime = LocalDateTime.now();
        var validity = currentTime.plusSeconds(validityInSeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Timestamp.valueOf(currentTime))
                .setExpiration(Timestamp.valueOf(validity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @TrackExecutionTime
    public boolean validateToken(String token) {
        try {
            var claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    @TrackExecutionTime
    public Authentication getAuthentication(String token) {
        var user = userService.getById(getId(token));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());

    }

    @TrackExecutionTime
    public UUID getId(String token) {
        String id = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getId();
        return UUID.fromString(id);
    }

    @TrackExecutionTime
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }
}

