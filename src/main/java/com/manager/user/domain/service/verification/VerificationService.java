package com.manager.user.domain.service.verification;


import com.manager.finance.metric.TrackExecutionTime;
import com.manager.user.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationService {
    private final EmailVerificationService emailVerificationService;
    private final PhoneVerificationService phoneVerificationService;

    @TrackExecutionTime
    public void createVerification(UserModel user) {
        emailVerificationService.createAndSaveVerification(user);
        phoneVerificationService.createAndSaveVerification(user);
    }

}