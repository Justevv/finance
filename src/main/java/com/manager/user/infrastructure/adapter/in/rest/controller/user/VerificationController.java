package com.manager.user.infrastructure.adapter.in.rest.controller.user;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.application.port.in.ConfirmEmailUseCase;
import com.manager.user.domain.service.verification.PhoneVerificationService;
import com.manager.user.infrastructure.adapter.out.persistence.mapper.UserPrincipalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/verification")
@Slf4j
@RequiredArgsConstructor
public class VerificationController {
    private final ConfirmEmailUseCase confirmEmailUseCase;
    private final PhoneVerificationService phoneVerificationService;
    private final UserPrincipalMapper principalMapper;

    @PostMapping("/{userId}/phone")
    @TrackExecutionTime
    public ResponseEntity<Map<String, Boolean>> confirmPhone(@PathVariable("userId") UUID user, @RequestParam String code) {
        log.debug("User {} tries to verify phone", user);
        var isConfirmed = phoneVerificationService.verifyPhone(user, code);
        var response = Map.of("Phone confirmed", isConfirmed);
        log.debug("Is user {} confirm his phone: {}", user, isConfirmed);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/email")
    @TrackExecutionTime
    public ResponseEntity<Map<String, Boolean>> confirmEmail(Principal principal, @RequestParam String code) {
        log.debug("User {} tries to verify email", principal);
        var isConfirmed = confirmEmailUseCase.verifyEmail(principalMapper.toModel(principal), code);
        var response = Map.of("Email confirmed", isConfirmed);
        log.debug("Is user {} confirm his email: {}", principal, isConfirmed);
        return ResponseEntity.ok(response);
    }
}