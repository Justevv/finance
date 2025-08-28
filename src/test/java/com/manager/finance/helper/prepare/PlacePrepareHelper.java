package com.manager.finance.helper.prepare;

import com.manager.finance.entity.PlaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.UUID;

@TestConfiguration
public class PlacePrepareHelper {
    @Autowired
    private UserPrepareHelper userPrepareHelper;

    public PlaceEntity createPlace() {
        var placeEntity = new PlaceEntity();
        placeEntity.setId(UUID.randomUUID());
        placeEntity.setName("placeName");
        placeEntity.setAddress("placeAddress");
        return placeEntity;
    }
}

