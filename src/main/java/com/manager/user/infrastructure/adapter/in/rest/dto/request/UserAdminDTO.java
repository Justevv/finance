package com.manager.user.infrastructure.adapter.in.rest.dto.request;

import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserAdminDTO extends UserDTO {
    private boolean isPhoneConfirmed;
    private boolean isEmailConfirmed;
    private Collection<RoleEntity> roles;

}
