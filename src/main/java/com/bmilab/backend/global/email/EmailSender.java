package com.bmilab.backend.global.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {
    private static final String ORGANIZATION_NAME = "SNUH BMI Lab";

    private final MailProperties mailProperties;

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendAsync(String email, String title, String username, String password, String template) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(new InternetAddress(mailProperties.getUsername(), ORGANIZATION_NAME));
            messageHelper.setTo(email);
            messageHelper.setSubject("[SNUH BMI Lab] " + title);
            messageHelper.setReplyTo(mailProperties.getUsername());
            messageHelper.setText(template, true);

            message.addHeader("Precedence", "normal");
            message.addHeader("X-Auto-Response-Suppress", "OOF, AutoReply");
            message.addHeader("Message-ID", generateMessageId());

            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    public void sendAccountCreateEmailAsync(String email, String username, String password) {
        sendAsync(email, "계정 생성 안내", username, password, getAccountCreateTemplate(username, password));
    }

    public void sendFindPasswordEmailAync(String email, String username, String password) {
        sendAsync(email, "비밀번호 재설정 안내", username, password, getFindPasswordTemplate(username, password));
    }

    private String generateMessageId() {
        return "<" + UUID.randomUUID() + "@%s>".formatted(mailProperties.getUsername().split("@")[1]);
    }

    public String getAccountCreateTemplate(String username, String password) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("password", password);

        return templateEngine.process("email/account-create-template", context);
    }

    public String getFindPasswordTemplate(String username, String password) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("password", password);

        return templateEngine.process("email/find-password-template", context);
    }
}
