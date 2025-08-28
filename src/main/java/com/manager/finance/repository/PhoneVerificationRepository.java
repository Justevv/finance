package com.manager.finance.repository;

import com.manager.finance.entity.PhoneVerificationEntity;
import com.manager.finance.entity.UserEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneVerificationRepository extends JpaRepository<PhoneVerificationEntity, UUID> {
    Optional<PhoneVerificationEntity> findByUser(UserEntity user);

    @Modifying
    @Query("DELETE FROM PhoneVerificationEntity pve WHERE pve.expireTime < ?1")
    void deleteByExpireTimeBefore(LocalDateTime dateTime);

    List<PhoneVerificationEntity> findByIsSent(boolean isSent, Limit limit);
}
