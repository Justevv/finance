package com.manager.finance.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendHTMLMessage(String subject, String messageText, String receiver) throws MessagingException;
}
