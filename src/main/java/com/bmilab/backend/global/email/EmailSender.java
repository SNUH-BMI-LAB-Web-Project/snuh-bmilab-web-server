package com.bmilab.backend.global.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
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
    public void sendAsyncWithTemplate(String email, String title, String template) {
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

    @Async
    public void sendAsyncWithFile(String email, String title, String fileName, ByteArrayInputStream attachment) throws IOException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(new InternetAddress(mailProperties.getUsername(), ORGANIZATION_NAME));
            messageHelper.setTo(email);
            messageHelper.setSubject("[SNUH BMI Lab] " + title);
            messageHelper.setReplyTo(mailProperties.getUsername());

            messageHelper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"),
                    new ByteArrayResource(IOUtils.toByteArray(attachment)));
            message.addHeader("Precedence", "normal");
            message.addHeader("X-Auto-Response-Suppress", "OOF, AutoReply");
            message.addHeader("Message-ID", generateMessageId());

            javaMailSender.send(message);
        } catch (IOException | MessagingException exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    public void sendAccountCreateEmailAsync(String email, String username, String password) {
        sendAsyncWithTemplate(email, "계정 생성 안내", getAccountCreateTemplate(username, password));
    }

    public void sendFindPasswordEmailAsync(String email, String username, String password) {
        sendAsyncWithTemplate(email, "비밀번호 재설정 안내", getFindPasswordTemplate(username, password));
    }

    public void sendReportEmailAsync(String email, LocalDate date, ByteArrayInputStream excelFile) throws IOException {
        sendAsyncWithFile(email, date + " 업무일지 보고드립니다.", "일일 업무 보고_" + date + ".xlsx", excelFile);
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
