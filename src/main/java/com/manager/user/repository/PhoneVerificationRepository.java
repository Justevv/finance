package com.manager.user.repository;

import com.manager.user.entity.PhoneVerificationEntity;
import com.manager.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneVerificationRepository extends JpaRepository<PhoneVerificationEntity, UUID> {
    Optional<PhoneVerificationEntity> findByUser(UserEntity user);

    @Modifying
    @Query("DELETE FROM PhoneVerificationEntity pve WHERE pve.expireTime < ?1")
    void deleteByExpireTimeBefore(LocalDateTime dateTime);

    @Query(value = """
            SELECT id,
                code,
                expire_time,
                user_id,
                is_sent
            FROM phone_verification
            WHERE NOT is_sent
            FOR UPDATE SKIP LOCKED
            LIMIT :limit""", nativeQuery = true)
    List<PhoneVerificationEntity> findByIsSent(@Param("limit") int limit);
}
