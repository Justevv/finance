package com.manager.user.infrastructure.adapter.in.rest.mapper;

import com.manager.user.domain.model.UserModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.admin.UserUpdateRequestAdminDto;
import com.manager.user.infrastructure.adapter.in.rest.dto.response.UserAdminResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserUpdateAdminDtoMapper implements DtoMapper<UserUpdateRequestAdminDto, UserAdminResponseDto, UserModel> {

    @Override
    public UserModel toModel(UserUpdateRequestAdminDto dto) {
        if (dto == null) {
            return null;
        }
        return UserModel.builder()
                .username(dto.user().username())
                .password(dto.user().password())
                .phone(dto.user().phone())
                .email(dto.user().email())
                .isEmailConfirmed(dto.isEmailConfirmed())
                .isPhoneConfirmed(dto.isPhoneConfirmed())
//                .roles(dto.roles())
                .build();
    }

    @Override
    public UserAdminResponseDto toResponseDto(UserModel dto) {
        if (dto == null) {
            return null;
        }
        return UserAdminResponseDto.builder()
                .id(dto.id())
                .username(dto.username())
                .phone(dto.phone())
                .email(dto.email())
                .isEmailConfirmed(dto.isEmailConfirmed())
                .isPhoneConfirmed(dto.isPhoneConfirmed())
                .roles(dto.roles())
                .build();
    }

}
