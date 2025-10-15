package com.manager.user.application.port.in;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserAdminUseCase {

    List<UserModel> getAll();

    UserModel get(UUID user);

    @Transactional
    UserModel create(UserModel userAdminDTO);

    @Transactional
    @TrackExecutionTime
    UserModel update(UUID uuid, UserModel input);

    @Transactional
    @TrackExecutionTime
    void delete(UUID userId);
}
