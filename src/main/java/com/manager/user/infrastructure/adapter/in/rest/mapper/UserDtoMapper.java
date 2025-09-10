package com.manager.user.infrastructure.adapter.in.rest.mapper;

import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.UserRequestDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDtoMapper implements DtoMapper<UserRequestDto, UserResponseDto, UserModel> {

    @Override
    public UserModel toModel(UserRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return UserModel.builder()
                .username(dto.username())
                .password(dto.password())
                .phone(dto.phone())
                .email(dto.email())
                .build();
    }

    @Override
    public UserResponseDto toResponseDto(UserModel dto) {
        if (dto == null) {
            return null;
        }
        return UserResponseDto.builder()
                .id(dto.id())
                .username(dto.username())
                .phone(dto.phone())
                .email(dto.email())
                .isEmailConfirmed(dto.isEmailConfirmed())
                .isPhoneConfirmed(dto.isEmailConfirmed())
                .build();
    }

}
