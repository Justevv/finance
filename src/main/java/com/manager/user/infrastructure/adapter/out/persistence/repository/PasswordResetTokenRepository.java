package com.manager.user.infrastructure.adapter.out.persistence.repository;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PasswordResetTokenEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, UUID> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

    List<PasswordResetTokenEntity> findByUser(UserEntity user);

    @Modifying
    @Query("DELETE FROM PasswordResetTokenEntity prt WHERE prt.expireTime < ?1")
    void deleteByExpireTimeBefore(LocalDateTime dateTime);
}
