package com.manager.finance.model;

import com.manager.finance.dto.PasswordUpdateDTO;
import com.manager.finance.entity.PasswordResetToken;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.event.ResetPasswordEvent;
import com.manager.finance.exception.PasswordResetTokenNotFoundException;
import com.manager.finance.repository.PasswordResetTokenRepository;
import com.manager.finance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PasswordModel {
    private static final String PASSWORD_RESET_TOKEN_DOES_NOT_EXISTS_ERROR_MESSAGE = "Password reset token doesn't exists";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
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

    private PasswordResetToken createPasswordResetToken(UserEntity user) {
        log.debug("Create PasswordResetToken for user {}", user);
        var token = UUID.randomUUID().toString();
        var passwordResetToken = new PasswordResetToken(token, user, resetPasswordTokenExpiredTime);
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
