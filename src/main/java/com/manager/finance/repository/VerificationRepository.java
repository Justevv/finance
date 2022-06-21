package com.manager.finance.repository;

import com.manager.finance.entity.UserEntity;
import com.manager.finance.entity.VerificationEntity;
import com.manager.finance.entity.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<VerificationEntity, Long> {
    Optional<VerificationEntity> findByUserAndType(UserEntity user, VerificationType type);
}
