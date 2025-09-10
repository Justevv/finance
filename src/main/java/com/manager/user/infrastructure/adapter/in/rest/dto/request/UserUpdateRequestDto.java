package com.manager.user.infrastructure.adapter.in.rest.dto.request;

import com.manager.finance.infrastructure.adapter.in.rest.dto.validator.EmailValidator;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.ToString;

@Builder
public record UserUpdateRequestDto(
        String username,
        @ToString.Exclude
        String password,
        @Email(message = "email", regexp = EmailValidator.EMAIL_REGEXP)
        String email,
        String phone) {

}
