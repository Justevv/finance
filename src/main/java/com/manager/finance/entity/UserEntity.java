package com.manager.finance.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "users")
@Data
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotNull
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
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
    @Transient
    private String role;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
//        return isEmailConfirmed && isPhoneConfirmed;
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
