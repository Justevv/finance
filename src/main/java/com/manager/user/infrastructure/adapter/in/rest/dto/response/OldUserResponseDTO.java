package com.manager.user.infrastructure.adapter.in.rest.dto.response;

import com.manager.user.infrastructure.adapter.out.persistence.entity.UserEntity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OldUserResponseDTO {
    @Id
    @Indexed
    private UUID id;
    private String username;
    private String email;
    private String phone;


    public static OldUserResponseDTO fromUser(UserEntity user) {
        return OldUserResponseDTO.builder()
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

}
