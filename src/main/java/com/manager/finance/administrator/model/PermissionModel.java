package com.manager.finance.administrator.model;

import com.manager.finance.entity.PermissionEntity;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PermissionModel {
    private static final String ROLE_LOG_NAME = "role";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(ROLE_LOG_NAME);

    @Autowired
    private PermissionRepository permissionRepository;

    public List<PermissionEntity> getPermissions() {
        return permissionRepository.findAll();
    }


}
