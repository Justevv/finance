package com.manager.user.controller;

import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.service.verification.EmailVerificationService;
import com.manager.user.service.verification.PhoneVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/verification/{userId}")
@Slf4j
@RequiredArgsConstructor
public class Verification {
    private final EmailVerificationService emailVerificationService;
    private final PhoneVerificationService phoneVerificationService;

    @PostMapping("/phone")
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
    public ResponseEntity<Map<String, Boolean>> confirmEmail(@PathVariable("userId") UUID user, @RequestParam String code) {
        log.debug("User {} tries to verify email", user);
        var isConfirmed = emailVerificationService.verifyEmail(user, code);
        var response = Map.of("Email confirmed", isConfirmed);
        log.debug("Is user {} confirm his email: {}", user, isConfirmed);
        return ResponseEntity.ok(response);
    }
}