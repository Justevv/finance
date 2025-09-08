package com.manager.user.service.message;

import com.manager.user.infrastructure.adapter.out.persistence.entity.EmailVerificationEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendEmailService {
    private final EmailService emailService;

    @SneakyThrows
    public void sendConfirmEmail(EmailVerificationEntity verification) {
        log.debug("Trying to send an email verification message");
        var subject = "Registration Confirmation";


        var messageText = verification.getCode();
        log.debug("Nem email {}", messageText);
        emailService.sendHTMLMessage(subject, messageText, verification.getUser().getEmail());
//        throw new UnsupportedOperationException("fix text letter and other");
    }
}
