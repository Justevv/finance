package com.manager.user.infrastructure.adapter.out.persistence.repository.impl;

import com.manager.user.application.port.out.repository.PhoneVerificationRepository;
import com.manager.user.domain.model.UserModel;
import com.manager.user.domain.model.VerificationModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PhoneVerificationEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.PhoneVerificationSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PhoneVerificationSpringDataRepositoryImp implements PhoneVerificationRepository {

    private final PhoneVerificationSpringDataRepository repository;
    private final EntityMapper<UserEntity, UserModel> userMapper;
    private final EntityMapper<PhoneVerificationEntity, VerificationModel> mapper;


    @Override
    public Optional<VerificationModel> findByUser(UserModel user) {
        return repository.findByUser(userMapper.toEntity(user)).map(mapper::toModel);
    }

    @Override
    public void deleteByExpireTimeBefore(LocalDateTime dateTime) {
        repository.deleteByExpireTimeBefore(dateTime);

    }

    @Override
    public List<VerificationModel> findByIsSent(int limit) {
        return repository.findByIsSent(limit).stream().map(mapper::toModel).toList();
    }

    @Override
    public void delete(VerificationModel emailVerificationCode) {
        repository.delete(mapper.toEntity(emailVerificationCode));
    }

    @Override
    public VerificationModel save(VerificationModel verificationModel) {
        return mapper.toModel(repository.save(mapper.toEntity(verificationModel)));
    }

    @Override
    public void saveAll(List<VerificationModel> verification) {
        repository.saveAll(verification.stream().map(mapper::toEntity).toList());
    }
}
