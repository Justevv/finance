package com.manager.user.domain.model;

import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.Builder;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record UserModel(
        UUID id,
        String username,
        @ToString.Exclude
        String password,
        String phone,
        String email,
        boolean isPhoneConfirmed,
        boolean isEmailConfirmed,
        Set<RoleEntity> roles
) implements Model {
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(roles);
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities(Collection<RoleEntity> roles) {
        var privileges = roles.stream()
                .flatMap(x -> x.getPermissions().stream().map(PermissionEntity::getPermission))
                .collect(Collectors.toList());
        roles.forEach(x -> privileges.add(x.getName()));
        return privileges.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
