package com.manager.user.infrastructure.adapter.out.persistence.mapper;

import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEntityMapper implements EntityMapper<UserEntity, UserModel> {

    @Override
    public UserModel toModel(UserEntity dto) {
        if (dto == null) {
            return null;
        }
        return UserModel.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .isEmailConfirmed(dto.isEmailConfirmed())
                .isPhoneConfirmed(dto.isPhoneConfirmed())
                .roles(dto.getRoles())
                .build();
    }

    @Override
    public UserEntity toEntity(UserModel dto) {
        if (dto == null) {
            return null;
        }
        return UserEntity.builder()
                .id(dto.id())
                .username(dto.username())
                .password(dto.password())
                .phone(dto.phone())
                .email(dto.email())
                .isEmailConfirmed(dto.isEmailConfirmed())
                .isPhoneConfirmed(dto.isPhoneConfirmed())
                .roles(dto.roles())
                .build();
    }

}
