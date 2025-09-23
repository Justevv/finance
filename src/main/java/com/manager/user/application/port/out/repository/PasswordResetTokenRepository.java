package com.manager.user.application.port.out.repository;

import com.manager.user.domain.model.PasswordResetTokenModel;
import com.manager.user.domain.model.UserModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository {

    Optional<PasswordResetTokenModel> findByTokenAndUser(String token, UserModel user);

    List<PasswordResetTokenModel> findByUser(UserModel user);

    void deleteByExpireTimeBefore(LocalDateTime dateTime);

    void deleteAll(List<PasswordResetTokenModel> tokens);

    PasswordResetTokenModel save(PasswordResetTokenModel passwordResetToken);

    void delete(PasswordResetTokenModel passwordResetToken);
}
