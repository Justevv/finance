package com.manager.finance.service;

import com.manager.finance.entity.VerificationEntity;
import com.manager.finance.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfirmationService {
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messages;
    @Autowired
    private EmailService emailService;

    @SneakyThrows
    public void sendConfirmEmail(VerificationEntity verification) {
        log.debug("Trying to send an email verification message");
        var subject = "Registration Confirmation";

        var messageText = verification.getCode();
        log.debug("Nem email {}", messageText);
        emailService.sendHTMLMessage(subject, messageText, verification.getUser().getEmail());
        throw new UnsupportedOperationException("fix text letter and other");
    }

    public void sendConfirmPhone(VerificationEntity verification) {
        log.debug("Trying to send a phone verification message");

        var recipientAddress = verification.getUser().getPhone();

        log.debug("New phoneVerificationCode {}", verification.getCode());
//        throw new UnsupportedOperationException("implement the logic");
    }

    public boolean isPhoneAlreadyConfirmed(String phone) {
        var phones = userRepository.findByPhoneAndIsPhoneConfirmed(phone, true);
        var isPhoneAlreadyConfirmed = phones.isPresent();
        log.debug("Is phone {} already confirmed: {}", phone, isPhoneAlreadyConfirmed);
        return isPhoneAlreadyConfirmed;
    }

    public boolean isEmailAlreadyConfirmed(String email) {
        var emailConfirmed = userRepository.findByEmailAndIsEmailConfirmed(email, true);
        var isEmailAlreadyConfirmed = emailConfirmed.isPresent();
        log.debug("Is email {} already confirmed: {}", email, isEmailAlreadyConfirmed);
        return isEmailAlreadyConfirmed;
    }

}
