package com.manager.user.infrastructure.adapter.out.persistence.repository.impl;

import com.manager.user.application.port.out.repository.PasswordResetTokenRepository;
import com.manager.user.domain.model.PasswordResetTokenModel;
import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PasswordResetTokenEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.PasswordResetTokenSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PasswordResetTokenSpringDataRepositoryImp implements PasswordResetTokenRepository {
    private final PasswordResetTokenSpringDataRepository repository;
    private final EntityMapper<PasswordResetTokenEntity, PasswordResetTokenModel> mapper;
    private final EntityMapper<UserEntity, UserModel> userMapper;

    @Override

    public Optional<PasswordResetTokenModel> findByTokenAndUser(String token, UserModel user) {
        return repository.findByTokenAndUserId(token, user.id()).map(mapper::toModel);
    }

    @Override
    public List<PasswordResetTokenModel> findByUser(UserModel user) {
        return repository.findByUser(userMapper.toEntity(user)).stream().map(mapper::toModel).toList();
    }

    @Override
    public void deleteByExpireTimeBefore(LocalDateTime dateTime) {
        repository.deleteByExpireTimeBefore(dateTime);
    }

    @Override
    public void deleteAll(List<PasswordResetTokenModel> tokens) {
        repository.deleteAll();
    }

    @Override
    public PasswordResetTokenModel save(PasswordResetTokenModel passwordResetToken) {
        return mapper.toModel(repository.save(mapper.toEntity(passwordResetToken)));
    }

    @Override
    public void delete(PasswordResetTokenModel passwordResetToken) {
        repository.deleteById(passwordResetToken.id());
    }
}
