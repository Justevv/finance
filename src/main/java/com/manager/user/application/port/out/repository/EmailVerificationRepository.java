package com.manager.user.application.port.out.repository;

import com.manager.user.domain.model.VerificationModel;
import com.manager.user.domain.model.UserModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailVerificationRepository {
    Optional<VerificationModel> findByUser(UserModel user);

    void deleteByExpireTimeBefore(LocalDateTime dateTime);

    List<VerificationModel> findByIsSent(int limit);

    void delete(VerificationModel emailVerificationCode);

    VerificationModel save(VerificationModel verificationModel);

    void saveAll(List<VerificationModel> verifications);
}
