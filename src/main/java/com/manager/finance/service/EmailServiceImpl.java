package com.manager.finance.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendHTMLMessage(String subject, String messageText, String receiver) throws MessagingException {
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        mimeMessage.setContent(messageText, "text/html");
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setFrom(sender);
        mailSender.send(mimeMessage);
    }
}
