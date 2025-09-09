package com.manager.user.domain.service.verification;

import com.manager.user.infrastructure.adapter.in.rest.dto.PasswordUpdateDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PasswordResetTokenEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.event.ResetPasswordEvent;
import com.manager.user.exception.PasswordResetTokenNotFoundException;
import com.manager.user.infrastructure.adapter.out.persistence.repository.PasswordResetTokenRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.manager.finance.constant.Constant.USER_EMAIL_DOES_NOT_EXISTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordService {
    private static final String PASSWORD_RESET_TOKEN_DOES_NOT_EXISTS_ERROR_MESSAGE = "Password reset token doesn't exists";
    private final UserSpringDataRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    @Value("${resetPasswordTokenExpiredTime}")
    private int resetPasswordTokenExpiredTime;

    @Transactional
    public boolean createPasswordResetToken(String email) {
        log.debug("User with email {} tries to reset password", email);
        var user = userRepository.findByEmailAndIsEmailConfirmed(email, true)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_DOES_NOT_EXISTS));
        var passwordToken = createPasswordResetToken(user);
        eventPublisher.publishEvent(new ResetPasswordEvent(passwordToken));
        return true;
    }

    private PasswordResetTokenEntity createPasswordResetToken(UserEntity user) {
        log.debug("Create PasswordResetToken for user {}", user);
        var token = UUID.randomUUID().toString();
        var passwordResetToken = new PasswordResetTokenEntity(token, user, resetPasswordTokenExpiredTime);
        passwordResetTokenRepository.deleteAll(passwordResetTokenRepository.findByUser(user));
        passwordResetTokenRepository.save(passwordResetToken);
        log.info("PasswordResetToken was created: {}", passwordResetToken);
        return passwordResetToken;
    }

    @Transactional
    public boolean validatePasswordResetToken(String token, PasswordUpdateDTO passwordUpdateDTO) {
        var passToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new PasswordResetTokenNotFoundException(PASSWORD_RESET_TOKEN_DOES_NOT_EXISTS_ERROR_MESSAGE));
        if (!passToken.isExpire()) {
            var user = passToken.getUser();
            user.setPassword(passwordEncoder.encode(passwordUpdateDTO.getPassword()));
            userRepository.save(user);
            passwordResetTokenRepository.delete(passToken);
            log.info("User password was updated {}", user);
            return true;
        }
        return false;
    }

}
