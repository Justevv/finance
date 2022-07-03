package com.manager.finance.administrator.dto;

import com.manager.finance.entity.PermissionEntity;
import lombok.Data;

import java.util.Collection;

@Data
public class RoleDTO {
    private String name;
    private Collection<PermissionEntity> permissions;

}
