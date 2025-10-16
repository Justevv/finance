package com.manager.user.application.port.out.repository;

import com.manager.user.domain.model.RoleModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {

    List<RoleModel> findAll();

    Optional<RoleModel> findById(UUID id);

    RoleModel getById(UUID id);
}
