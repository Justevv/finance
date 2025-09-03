package com.manager.user.repository;

import com.manager.user.entity.AuthenticationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLog, UUID> {
}
