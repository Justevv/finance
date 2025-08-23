package com.manager.finance.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "role")
@Data
public class RoleEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 9178661439456356177L;

    @Id
    private UUID guid;

    @Column(unique = true)
    private String name;

    @ElementCollection(targetClass = PermissionEntity.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_guid"))
    private Set<PermissionEntity> permissions;
}
