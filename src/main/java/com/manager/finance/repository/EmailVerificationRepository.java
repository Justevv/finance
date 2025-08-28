package com.manager.finance.repository;

import com.manager.finance.entity.EmailVerificationEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, UUID> {
    Optional<EmailVerificationEntity> findByUser(UserEntity user);

    @Modifying
    @Query("DELETE FROM EmailVerificationEntity eve WHERE eve.expireTime < ?1")
    void deleteByExpireTimeBefore(LocalDateTime dateTime);

    List<EmailVerificationEntity> findByIsSent(boolean isSent, Limit limit);
}
