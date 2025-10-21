package com.manager.user.infrastructure.adapter.out.persistence.repository.impl;

import com.manager.user.application.port.out.repository.RoleRepository;
import com.manager.user.domain.model.RoleModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.manager.user.infrastructure.adapter.out.persistence.exception.RoleNotFoundException;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.RoleSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleSpringDataRepositoryImp implements RoleRepository {

    private final RoleSpringDataRepository repository;
    private final EntityMapper<RoleEntity, RoleModel> mapper;


    @Override
    public List<RoleModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<RoleModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public RoleModel getById(UUID id) {
        return repository.findById(id).map(mapper::toModel).orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Override
    public RoleModel save(RoleModel model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
