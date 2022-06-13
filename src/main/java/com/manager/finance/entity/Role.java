package com.manager.finance.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

//    @ManyToMany
//    @JoinTable(
//            name = "roles_permission",
//            joinColumns = @JoinColumn(
//                    name = "role_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(
//                    name = "permissions_id", referencedColumnName = "id"))
//    private Collection<Permission> permissions;
}
