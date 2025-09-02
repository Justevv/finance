package com.manager.finance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "place")
@Getter
public class PlaceEntity extends CrudEntity {
    private String name;
    private String address;

}
