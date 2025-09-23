package com.manager.user.application.port.in;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.exception.UserAlreadyExistException;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface UserUseCase {

    UserModel findById(UUID id);

    UserModel create(UserModel userDTO) throws UserAlreadyExistException;

    UserModel update(UserModel principal, UserModel input);

    @Transactional
    @TrackExecutionTime
    UserModel updatePassword(UserModel principal, String password);

    void delete(UserModel principal);
}
