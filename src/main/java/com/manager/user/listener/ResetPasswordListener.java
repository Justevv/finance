package com.manager.user.listener;


import com.manager.user.event.ResetPasswordEvent;
import com.manager.user.domain.service.message.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordListener {
    private static final String PASSWORD_RESET_LINK = "/api/v1/user/password/reset?token=";
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${URI.application}")
    private String applicationURI;
    private final EmailService emailService;

    @Async
    @EventListener
    @SneakyThrows
    public void onApplicationEvent(ResetPasswordEvent event) {
        log.debug("Event start {}", event);

        var passwordResetToken = event.getPasswordToken();
        log.debug("Trying to send reset password email");
        var subject = "Reset password";
        var link = applicationURI + PASSWORD_RESET_LINK + passwordResetToken.getToken();

        var messageText = """
                <a href="$link">Reset password</a>"""
                .replace("$link", link);
        log.debug("Nem email {}", messageText);

        emailService.sendHTMLMessage(subject, messageText, event.getPasswordToken().getUser().getEmail());
        throw new UnsupportedOperationException("fix text letter and other");
    }


}
