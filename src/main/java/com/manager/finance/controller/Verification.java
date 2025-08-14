package com.manager.finance.controller;

import com.manager.finance.model.VerificationModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/verification/{userId}")
@Slf4j
@RequiredArgsConstructor
public class Verification {
    private final VerificationModel verificationModel;

    @PostMapping("/phone")
    public ResponseEntity<Map<String, Boolean>> confirmPhone(@PathVariable("userId") long user, @RequestParam String code) {
        log.debug("User {} tries to verify phone", user);
        var isConfirmed = verificationModel.verifyPhone(user, code);
        var response = Map.of("Phone confirmed", isConfirmed);
        log.debug("Is user {} confirm his phone: {}", user, isConfirmed);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/email")
    public ResponseEntity<Map<String, Boolean>> confirmEmail(@PathVariable("userId") long user, @RequestParam String code) {
        log.debug("User {} tries to verify email", user);
        var isConfirmed = verificationModel.verifyEmail(user, code);
        var response = Map.of("Email confirmed", isConfirmed);
        log.debug("Is user {} confirm his email: {}", user, isConfirmed);
        return ResponseEntity.ok(response);
    }
}