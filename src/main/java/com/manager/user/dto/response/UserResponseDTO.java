package com.manager.user.dto.response;

import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    @Id
    @Indexed
    private UUID id;
    private String username;
    private String email;
    private String phone;


    public static UserResponseDTO fromUser(UserEntity user) {
        return UserResponseDTO.builder()
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

}
