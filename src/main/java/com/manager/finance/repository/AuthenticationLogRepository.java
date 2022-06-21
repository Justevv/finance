package com.manager.finance.repository;

import com.manager.finance.entity.AuthenticationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLog, Long> {
}
