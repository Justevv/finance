package com.manager.user.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.ToString;


public record PasswordUpdateDTO(
        @NotBlank
        String token,
        @NotBlank
        String username,
        @NotBlank
        @ToString.Exclude
        String password) {

}
