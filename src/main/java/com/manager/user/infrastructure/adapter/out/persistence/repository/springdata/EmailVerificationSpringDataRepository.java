package com.manager.user.infrastructure.adapter.out.persistence.repository.springdata;

import com.manager.user.infrastructure.adapter.out.persistence.entity.EmailVerificationEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationSpringDataRepository extends JpaRepository<EmailVerificationEntity, UUID> {
    Optional<EmailVerificationEntity> findByUser(UserEntity user);

    @Modifying
    @Query("DELETE FROM EmailVerificationEntity eve WHERE eve.expireTime < ?1")
    void deleteByExpireTimeBefore(LocalDateTime dateTime);

    @Query(value = """
            SELECT id,
                code,
                expire_time,
                user_id,
                is_sent
            FROM email_verification
            WHERE NOT is_sent
            FOR UPDATE SKIP LOCKED
            LIMIT :limit""", nativeQuery = true)
    List<EmailVerificationEntity> findByIsSent(@Param("limit") int limit);
}
