package com.manager.user.domain.service.verification;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.in.PasswordUseCase;
import com.manager.user.application.port.out.repository.PasswordResetTokenRepository;
import com.manager.user.application.port.out.repository.UserRepository;
import com.manager.user.domain.exception.PasswordResetTokenExpiredException;
import com.manager.user.domain.exception.PasswordResetTokenNotFoundException;
import com.manager.user.domain.model.PasswordResetTokenModel;
import com.manager.user.domain.model.ProcessStatus;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.service.UserService;
import com.manager.user.infrastructure.adapter.in.rest.dto.PasswordUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.manager.finance.constant.Constant.USER_DOES_NOT_EXISTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordService implements PasswordUseCase {
    private static final String PASSWORD_RESET_TOKEN_DOES_NOT_EXISTS_ERROR_MESSAGE = "Password reset token doesn't exists";
    private static final String PASSWORD_RESET_TOKEN_EXPIRED_ERROR_MESSAGE = "Password reset token was expired";
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;
    @Value("${resetPasswordTokenExpiredTime}")
    private int resetPasswordTokenExpiredTime;

    @TrackExecutionTime
    @Transactional
    @Override
    public void createPasswordResetToken(UserModel userModel) {
        log.debug("User {} tries to reset password", userModel);
        var user = userRepository.findByUsername(userModel.username())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        log.debug("Create PasswordResetToken for user {}", user);
        var token = UUID.randomUUID().toString();
        var passwordResetToken = PasswordResetTokenModel.builder()
                .id(UUID.randomUUID())
                .token(token)
                .user(user)
                .expireTime(LocalDateTime.now().plusSeconds(resetPasswordTokenExpiredTime))
                .status(ProcessStatus.NEW)
                .build();
        passwordResetTokenRepository.deleteAll(passwordResetTokenRepository.findByUser(user));
        passwordResetTokenRepository.save(passwordResetToken);
        log.info("PasswordResetToken was created: {}", passwordResetToken);
    }

    @TrackExecutionTime
    @Transactional
    @Override
    public void validatePasswordResetToken(PasswordUpdateDTO passwordUpdateDTO) {
        var userModel = userRepository.findByUsername(passwordUpdateDTO.username())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));
        var passToken = passwordResetTokenRepository.findByTokenAndUser(passwordUpdateDTO.token(), userModel)
                .orElseThrow(() -> new PasswordResetTokenNotFoundException(PASSWORD_RESET_TOKEN_DOES_NOT_EXISTS_ERROR_MESSAGE));
        if (!passToken.isExpire()) {
            var user = passToken.user();
            userService.updatePassword(user, passwordUpdateDTO.password());
            passwordResetTokenRepository.delete(passToken);
            log.info("User password was updated {}", user);
        } else {
            throw new PasswordResetTokenExpiredException(PASSWORD_RESET_TOKEN_EXPIRED_ERROR_MESSAGE);
        }
    }

}
