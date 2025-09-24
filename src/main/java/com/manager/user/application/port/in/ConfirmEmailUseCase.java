package com.manager.user.application.port.in;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ConfirmEmailUseCase {

    @Transactional
    @TrackExecutionTime
    boolean verifyEmail(UUID userId, String code);

    @TrackExecutionTime
    void createAndSaveVerification(UserEntity user);

    @TrackExecutionTime
    void createAndSaveVerification(UserModel user);

    @TrackExecutionTime
    boolean isEmailAlreadyConfirmed(String email);
}
