package com.manager.user.application.port.in;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import com.manager.user.exception.UserAlreadyExistException;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.OldUserResponseDTO;
import jakarta.transaction.Transactional;

import java.security.Principal;
import java.util.UUID;

public interface UserUseCase {

    @TrackExecutionTime
    UserModel findById(UUID id);

    @TrackExecutionTime
    OldUserResponseDTO getUser(Principal principal);

    @Transactional
    @TrackExecutionTime
    UserModel create(UserModel userDTO) throws UserAlreadyExistException;

    @Transactional
    @TrackExecutionTime
    UserModel update(UserModel principal, UserModel input);

    @Transactional
    @TrackExecutionTime
    void delete(UserModel principal);
}
