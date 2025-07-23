package com.manager.finance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class CrudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crud_generator")
    @SequenceGenerator(name = "crud_generator", sequenceName = "crud_seq", allocationSize = 1, initialValue = 100)
    private long id;
    @NotNull
    @ManyToOne
    private UserEntity user;
}
