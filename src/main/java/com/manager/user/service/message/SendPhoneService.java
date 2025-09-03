package com.manager.user.service.message;

import com.manager.user.entity.PhoneVerificationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendPhoneService {

    public void sendConfirmPhone(PhoneVerificationEntity verification) {
        log.debug("Trying to send a phone verification message");

        var recipientAddress = verification.getUser().getPhone();

        log.debug("New phoneVerificationCode {}", verification.getCode());
//        throw new UnsupportedOperationException("implement the logic");
    }
}
