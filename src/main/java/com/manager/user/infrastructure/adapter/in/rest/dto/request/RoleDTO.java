package com.manager.user.infrastructure.adapter.in.rest.dto.request;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import lombok.Data;

import java.util.Collection;

@Data
public class RoleDTO {
    private String name;
    private Collection<PermissionEntity> permissions;

}
