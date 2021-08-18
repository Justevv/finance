package com.manager.entity;

import com.manager.dto.PlaceDTO;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "place")
@Data
public class PlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String address;

    public PlaceEntity() {
    }

    public PlaceEntity(PlaceDTO placeDTO) {
        this.name = placeDTO.getName();
        this.address = placeDTO.getAddress();
    }
}
