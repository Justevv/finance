package com.manager.finance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class CrudEntity implements Serializable {
    @Id
    private UUID id;
}
