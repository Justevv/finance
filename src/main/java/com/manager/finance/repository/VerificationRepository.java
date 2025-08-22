package com.manager.finance.repository;

import com.manager.finance.entity.UserEntity;
import com.manager.finance.entity.VerificationEntity;
import com.manager.finance.entity.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationRepository extends JpaRepository<VerificationEntity, Long> {
    Optional<VerificationEntity> findByUserAndType(UserEntity user, VerificationType type);

    @Modifying
    @Query("DELETE FROM VerificationEntity ve WHERE ve.expireTime < ?1")
    void deleteByExpireTimeBefore(LocalDateTime dateTime);
}
