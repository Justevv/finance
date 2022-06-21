package com.manager.finance;

import com.manager.finance.entity.VerificationEntity;
import com.manager.finance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfirmationHelper {
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messages;
    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmEmail(VerificationEntity verification) {
        log.debug("Try send email confirmation message");
        var recipientAddress = verification.getUser().getEmail();
        var subject = "Registration Confirmation";

        var email = new SimpleMailMessage();
        email.setFrom(sender);
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(String.valueOf(verification.getCode()));
        log.debug("Nem email {}", email);
        throw new UnsupportedOperationException("fix text letter and other");
//        mailSender.send(email);
    }

    public void sendConfirmPhone(VerificationEntity verification) {
        log.debug("Try send phone confirmation message");

        var recipientAddress = verification.getUser().getPhone();

        log.debug("New phoneVerificationCode {}", verification.getCode());
        throw new UnsupportedOperationException("implement the logic");
    }

    public boolean isPhoneAlreadyConfirmed(String phone) {
        var phones = userRepository.findByPhoneAndIsPhoneConfirmed(phone, true);
        var isPhoneAlreadyConfirmed = !phones.isEmpty();
        log.debug("Is phone {} already confirmed: {}", phone, isPhoneAlreadyConfirmed);
        return isPhoneAlreadyConfirmed;
    }

    public boolean isEmailAlreadyConfirmed(String email) {
        var emails = userRepository.findByEmailAndIsEmailConfirmed(email, true);
        var isEmailAlreadyConfirmed = !emails.isEmpty();
        log.debug("Is email {} already confirmed: {}", email, isEmailAlreadyConfirmed);
        return isEmailAlreadyConfirmed;
    }
}
