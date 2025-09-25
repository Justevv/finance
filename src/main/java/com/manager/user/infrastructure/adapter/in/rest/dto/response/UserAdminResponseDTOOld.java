package com.manager.user.infrastructure.adapter.in.rest.dto.response;

import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserAdminResponseDTOOld {
    private UUID id;
    private String username;
    private String email;
    private String phone;
    private boolean isPhoneConfirmed;
    private boolean isEmailConfirmed;
    private Collection<RoleEntity> roles;

}