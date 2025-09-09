package com.manager.user.domain.service.message;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendHTMLMessage(String subject, String messageText, String receiver) throws MessagingException;
}
