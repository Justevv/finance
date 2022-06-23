package com.manager.finance.repository;

import com.manager.finance.entity.PasswordResetToken;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(UserEntity user);
}
