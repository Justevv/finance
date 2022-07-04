package com.manager.finance.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "place")
@Data
public class PlaceEntity extends CrudEntity {
    private String name;
    private String address;

}
