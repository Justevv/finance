package com.manager.finance.infrastructure.adapter.out.persistence.repository.impl;

import com.manager.finance.application.port.out.repository.PlaceRepository;
import com.manager.finance.domain.exception.EntityNotFoundException;
import com.manager.finance.domain.model.PlaceModel;
import com.manager.finance.infrastructure.adapter.out.persistence.entity.PlaceEntity;
import com.manager.finance.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.finance.infrastructure.adapter.out.persistence.repository.springdata.PlaceSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.manager.finance.constant.Constant.PLACE_ENTITY;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlaceSpringDataRepositoryImplements implements PlaceRepository {
    private final PlaceSpringDataRepository repository;
    private final EntityMapper<PlaceEntity, PlaceModel> mapper;

    @Override
    public Optional<PlaceModel> findByName(String name) {
        return repository.findByName(name).map(mapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public PlaceModel getById(UUID id) {
        Optional<PlaceEntity> place = repository.findById(id);
        if (place.isPresent()) {
            return mapper.toModel(place.get());
        } else {
            throw new EntityNotFoundException(PLACE_ENTITY, id);
        }
    }

    @Override
    public Optional<PlaceModel> findById(UUID id) {
        Optional<PlaceEntity> place = repository.findById(id);
        return place.map(mapper::toModel);
    }

    @Override
    public List<PlaceModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public PlaceModel save(PlaceModel place) {
        PlaceEntity saved = repository.save(mapper.toEntity(place));
        return mapper.toModel(saved);
    }
}
