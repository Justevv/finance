package com.manager.user.application.port.out.repository;

import com.manager.user.domain.model.RoleModel;

import java.util.List;

public interface RoleRepository {

    List<RoleModel> findAll();
}
