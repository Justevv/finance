package com.manager.user.repository;

import com.manager.user.entity.PasswordResetToken;
import com.manager.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(UserEntity user);

    @Modifying
    @Query("DELETE FROM PasswordResetToken prt WHERE prt.expireTime < ?1")
    void deleteByExpireTimeBefore(LocalDateTime dateTime);
}
