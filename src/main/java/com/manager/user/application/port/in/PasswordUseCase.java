package com.manager.user.application.port.in;

import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.PasswordUpdateDTO;

public interface PasswordUseCase {

    void createPasswordResetToken(UserModel userModel);

    void validatePasswordResetToken(PasswordUpdateDTO passwordUpdateDTO);
}
