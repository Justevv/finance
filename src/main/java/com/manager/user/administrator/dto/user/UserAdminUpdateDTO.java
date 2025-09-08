package com.manager.user.administrator.dto.user;

import com.manager.user.infrastructure.adapter.in.rest.dto.UserUpdateDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserAdminUpdateDTO extends UserUpdateDTO {
    private boolean isPhoneConfirmed;
    private boolean isEmailConfirmed;
    private Collection<RoleEntity> roles;

}