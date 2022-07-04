package com.manager.finance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
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
    @Column(unique = true)
    private String phone;
    @NotNull
    @NotBlank
    @Column(unique = true)
    private String email;
    @ColumnDefault("0")
    private BigDecimal balance = BigDecimal.ZERO;
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
        return isEmailConfirmed && isPhoneConfirmed;
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
                .flatMap(x -> x.getPermissions().stream().map(PermissionEntity::getName))
                .collect(Collectors.toList());
        roles.forEach(x -> privileges.add(x.getName()));
        return privileges.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
