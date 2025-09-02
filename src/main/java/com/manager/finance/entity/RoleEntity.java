package com.manager.finance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "role")
@Getter
public class RoleEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 9178661439456356177L;

    @Id
    private UUID id;

    @Column(unique = true)
    private String name;

    @ElementCollection(targetClass = PermissionEntity.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"))
    private Set<PermissionEntity> permissions;
}
