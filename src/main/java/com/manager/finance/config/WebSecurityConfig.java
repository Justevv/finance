package com.manager.finance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.finance.dto.UserDTO;
import com.manager.finance.entity.User;
import com.manager.finance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig {
    private static final int STRENGTH_PASSWORD = 8;
//    @Autowired
//    private AuthenticationEntryPoint authEntryPoint;
    @Autowired
    private UserService userService;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Value("${mainPage.path}")
    private String mainPagePath;
    @Value("${css.path}")
    private String cssPath;
    @Value("${js.path}")
    private String jsPath;
    @Value("${images.path}")
    private String imagesPath;
    @Value("${swagger.path}")
    private String swaggerPath;
    @Value("${swaggerResources.path}")
    private String webjarsPath;
    @Value("${webjars.path}")
    private String swaggerResourcesPath;
    @Value("${apiDocs.path}")
    private String apiDocsPath;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
//                .authenticationEntryPoint(authEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/login", mainPagePath, cssPath, jsPath, imagesPath).permitAll()
                .antMatchers(swaggerPath, webjarsPath, swaggerResourcesPath, apiDocsPath).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(successHandler())
                .and()
                .logout()
                .logoutSuccessHandler(onLogoutSuccess());
        return http.build();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH_PASSWORD);
    }

    private AuthenticationSuccessHandler successHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            UserDTO userDTO = new UserDTO(user.getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
            CloseableThreadContext.put(Log.SESSION_ID_KEY,
                    RequestContextHolder.currentRequestAttributes().getSessionId());
            ObjectMapper mapper = new ObjectMapper();
            String jsonUsername = mapper.writeValueAsString(userDTO);
            response.setContentType("text/x-json;charset=UTF-8");
            response.getWriter().write(jsonUsername);  // NOSONAR
            log.info("Session {} for user {} created",
                    RequestContextHolder.currentRequestAttributes().getSessionId(), userDTO);
        };
    }

    private LogoutSuccessHandler onLogoutSuccess() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) ->
                response.setStatus(HttpServletResponse.SC_OK);
    }

}
