package com.manager.finance.helper.converter;

import com.manager.finance.infrastructure.persistace.entity.PlaceEntity;
import com.manager.finance.infrastructure.persistace.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

@TestConfiguration
public class PlaceIdConverter implements Converter<String, PlaceEntity> {
    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public PlaceEntity convert(String source) {
        return placeRepository.findById(UUID.fromString(source)).orElseThrow();
    }
}
