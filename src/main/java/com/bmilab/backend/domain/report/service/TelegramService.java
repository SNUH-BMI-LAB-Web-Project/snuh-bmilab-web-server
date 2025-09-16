package com.bmilab.backend.domain.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class TelegramService {

    private final RestClient restClient;

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.chat-id}")
    private String chatId;

    public TelegramService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.telegram.org")
                .build();
    }

    public void sendMessage(String text) {
        restClient.post()
                .uri("/bot{token}/sendMessage", botToken)
                .body(Map.of(
                        "chat_id", chatId,
                        "text", text,
                        "parse_mode", "MarkdownV2",
                        "disable_web_page_preview", true
                ))
                .retrieve()
                .toBodilessEntity();
    }
}