package com.manager.finance.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "place")
@Data
public class PlaceEntity implements CrudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String address;
    @ManyToOne
    private User user;

}
