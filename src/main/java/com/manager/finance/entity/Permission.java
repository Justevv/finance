package com.manager.finance.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "permission")
@Data
public class Permission implements Serializable {
    @Serial
    private static final long serialVersionUID = 9178661439383356177L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    public Permission() {
    }

    public Permission(String name) {
        this.name = name;
    }
//    @ManyToMany(mappedBy = "permissions")
//    @JsonIgnoreProperties("permissions")
//    private Collection<Role> roles;

}
