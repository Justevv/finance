package com.manager.user.administrator.dto.user;

import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserAdminResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String phone;
    private boolean isPhoneConfirmed;
    private boolean isEmailConfirmed;
    private Collection<RoleEntity> roles;

}