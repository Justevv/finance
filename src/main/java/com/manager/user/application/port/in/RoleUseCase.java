package com.manager.user.application.port.in;

import com.manager.user.domain.exception.UserAlreadyExistException;
import com.manager.user.domain.model.RoleModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.RoleRequestDto;

import java.util.List;
import java.util.UUID;

public interface RoleUseCase {

    List<RoleModel> getAll();

    RoleModel get(UUID id);

    RoleModel create(RoleModel model) throws UserAlreadyExistException;

    RoleModel update(UUID roleId, RoleRequestDto model) throws UserAlreadyExistException;

    void delete(UUID id) throws UserAlreadyExistException;
}
