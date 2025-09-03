package com.manager.user.administrator.dto.user;

import com.manager.user.dto.response.UserResponseDTO;
import com.manager.user.entity.RoleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserAdminResponseDTO extends UserResponseDTO {
    private boolean isPhoneConfirmed;
    private boolean isEmailConfirmed;
    private Collection<RoleEntity> roles;

}