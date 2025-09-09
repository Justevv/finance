package com.manager.user.infrastructure.adapter.in.rest.dto.request;

import com.manager.finance.infrastructure.adapter.in.rest.dto.validator.EmailValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.ToString;

@Builder
public record UserRequestDTO(
        @NotBlank
        String username,
        @ToString.Exclude
        @NotBlank
        String password,
        @NotBlank
        @Email(message = "email", regexp = EmailValidator.EMAIL_REGEXP)
        String email,
        @NotBlank
        String phone) {
}
