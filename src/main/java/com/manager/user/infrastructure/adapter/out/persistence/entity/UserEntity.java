package com.manager.user.infrastructure.adapter.out.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserEntity implements UserDetails {
    @Id
    private UUID id;
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
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
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
