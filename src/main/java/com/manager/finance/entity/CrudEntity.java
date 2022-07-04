package com.manager.finance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class CrudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull
    @ManyToOne
    private UserEntity user;
}
