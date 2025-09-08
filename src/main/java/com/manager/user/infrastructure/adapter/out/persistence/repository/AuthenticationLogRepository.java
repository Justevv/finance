package com.manager.user.infrastructure.adapter.out.persistence.repository;

import com.manager.user.infrastructure.adapter.out.persistence.entity.AuthenticationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLogEntity, UUID> {
}
