package com.manager.finance.infrastructure.persistace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "place")
@Getter
@Setter
public class PlaceEntity extends CrudEntity {
    private String name;
    private String address;

}
