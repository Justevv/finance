package com.manager.user.application.port.in;

import com.manager.user.domain.exception.UserAlreadyExistException;
import com.manager.user.domain.model.UserModel;

import java.util.UUID;

public interface UserUseCase {

    UserModel findById(UUID id);

    UserModel create(UserModel userDTO) throws UserAlreadyExistException;

    UserModel update(UserModel principal, UserModel input);

    void delete(UserModel principal);
}
