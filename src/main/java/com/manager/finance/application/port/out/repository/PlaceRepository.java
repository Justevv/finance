package com.manager.finance.application.port.out.repository;

import com.manager.finance.domain.model.PlaceModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository {
    Optional<PlaceModel> findByName(String name);

    void deleteById(UUID id);

    PlaceModel getById(UUID id);

    Optional<PlaceModel> findById(UUID id);

    List<PlaceModel> findAll();

    PlaceModel save(PlaceModel place);
}
