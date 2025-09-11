package com.manager.user.infrastructure.adapter.out.persistence.repository.impl;

import com.manager.user.application.port.out.repository.UserRepository;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.exception.UserNotFoundException;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserSpringDataRepositoryImp implements UserRepository {

    private final UserSpringDataRepository repository;
    private final EntityMapper<UserEntity, UserModel> mapper;

    @Override
    public Optional<UserModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public UserModel getById(UUID id) {
        return repository.findById(id).map(mapper::toModel).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserModel save(UserModel user) {
        return mapper.toModel(repository.save(mapper.toEntity(user)));
    }

    @Override
    public void delete(UserModel user) {
        repository.delete(mapper.toEntity(user));
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toModel);
    }

    @Override
    public List<UserModel> findByEmail(String email) {
        return repository.findByEmail(email).stream().map(mapper::toModel).toList();
    }

    @Override
    public List<UserModel> findByPhone(String phone) {
        return repository.findByPhone(phone).stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<UserModel> findByEmailAndIsEmailConfirmed(String email, boolean isConfirmed) {
        return repository.findByEmailAndIsEmailConfirmed(email, isConfirmed).map(mapper::toModel);
    }

    @Override
    public Optional<UserModel> findByPhoneAndIsPhoneConfirmed(String phone, boolean isConfirmed) {
        return repository.findByPhoneAndIsPhoneConfirmed(phone, isConfirmed).map(mapper::toModel);
    }

}
