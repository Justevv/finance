package com.manager.user.domain.service.admin;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PermissionService {

    public List<PermissionEntity> getPermissions() {
        return List.of(PermissionEntity.values());
    }


}
