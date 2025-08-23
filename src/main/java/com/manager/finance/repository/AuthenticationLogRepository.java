package com.manager.finance.repository;

import com.manager.finance.entity.AuthenticationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLog, UUID> {
}
