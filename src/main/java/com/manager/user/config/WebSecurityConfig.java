package com.manager.user.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class WebSecurityConfig {
    private static final String CREATE_USER_API = "/v1/user";
    private static final String LOGIN_API = "/v1/auth/login";
    private static final String VERIFICATION_API = "/v1/verification/**";
    private static final String RESET_PASSWORD_API = "/v1/user/password/reset";
    private static final String FORGET_PASSWORD_API = "/v1/user/password/forget";
    private final JwtConfigure jwtConfigure;
    @Value("${path.mainPage}")
    private String mainPagePath;
    @Value("${path.css}")
    private String cssPath;
    @Value("${path.js}")
    private String jsPath;
    @Value("${path.images}")
    private String imagesPath;
    @Value("${path.swagger}")
    private String swaggerPath;
    @Value("${path.swaggerResources}")
    private String webjarsPath;
    @Value("${path.webjars}")
    private String swaggerResourcesPath;
    @Value("${path.apiDocs}")
    private String apiDocsPath;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(mainPagePath, cssPath, jsPath, imagesPath).permitAll()
                        .requestMatchers(swaggerPath, webjarsPath, swaggerResourcesPath, apiDocsPath).permitAll()
//                        .requestMatchers("/**").permitAll()
                        .requestMatchers(HttpMethod.POST, CREATE_USER_API, LOGIN_API, VERIFICATION_API).permitAll()
                        .requestMatchers(HttpMethod.POST, FORGET_PASSWORD_API, RESET_PASSWORD_API).permitAll()
                        .anyRequest().authenticated()
                )
                .apply(jwtConfigure);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
