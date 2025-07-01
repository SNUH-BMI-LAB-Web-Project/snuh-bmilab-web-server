package com.bmilab.backend.global.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {
    private static final String ORGANIZATION_NAME = "SNUH BMI Lab";

    private final MailProperties mailProperties;

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendAsync(String email, String username, String password) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(new InternetAddress(mailProperties.getUsername(), ORGANIZATION_NAME));
            messageHelper.setTo(email);
            messageHelper.setSubject("[SNUH BMI Lab] 계정 생성 안내");
            messageHelper.setReplyTo(mailProperties.getUsername());
            messageHelper.setText(setContext(username, password), true);

            message.addHeader("Precedence", "normal");
            message.addHeader("X-Auto-Response-Suppress", "OOF, AutoReply");
            message.addHeader("Message-ID", generateMessageId());

            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException exception) {
            log.error(exception.getMessage(), exception);
        }
    }


    private String generateMessageId() {
        return "<" + UUID.randomUUID() + "@%s>".formatted(mailProperties.getUsername().split("@")[1]);
    }

    public String setContext(String username, String password) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("password", password);

        return templateEngine.process("email/account-create-template", context);
    }
}
