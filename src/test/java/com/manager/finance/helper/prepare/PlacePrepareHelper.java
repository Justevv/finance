package com.manager.finance.helper.prepare;

import com.manager.finance.infrastructure.adapter.out.persistence.entity.PlaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.UUID;

@TestConfiguration
public class PlacePrepareHelper {
    @Autowired
    private UserPrepareHelper userPrepareHelper;

    public PlaceEntity createPlace() {
        var placeEntity = PlaceEntity.builder()
                .build();
        placeEntity.setId(UUID.randomUUID());
        placeEntity.setName("placeName");
        placeEntity.setAddress("placeAddress");
        return placeEntity;
    }
}

