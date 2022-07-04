package com.manager.finance.administrator.dto.user;

import com.manager.finance.dto.UserDTO;
import com.manager.finance.entity.RoleEntity;
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
