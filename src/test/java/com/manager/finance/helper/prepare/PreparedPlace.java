package com.manager.finance.helper.prepare;

import com.manager.finance.entity.PlaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class PreparedPlace {
    @Autowired
    private PreparedUser preparedUser;

    public PlaceEntity createPlace() {
        var placeEntity = new PlaceEntity();
        placeEntity.setId(1);
        placeEntity.setName("placeName");
        placeEntity.setAddress("placeAddress");
        placeEntity.setUser(preparedUser.createUser());
        return placeEntity;
    }
}

