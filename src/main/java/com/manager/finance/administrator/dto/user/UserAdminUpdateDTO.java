package com.manager.finance.administrator.dto.user;

import com.manager.finance.dto.user.UserUpdateDTO;
import com.manager.finance.entity.RoleEntity;
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