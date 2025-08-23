package com.manager.finance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
public class UserEntity implements UserDetails {
    @Id
    private UUID guid;
    @NotNull
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotNull
    @NotBlank
    @ToString.Exclude
    private String password;
    @NotNull
    @NotBlank
    private String phone;
    @NotNull
    @NotBlank
    private String email;
    private boolean isPhoneConfirmed;
    private boolean isEmailConfirmed;
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("roles")
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_guid"),
            inverseJoinColumns = @JoinColumn(name = "role_guid"))
    private Set<RoleEntity> roles;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    @Transactional
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
