package com.bmilab.backend.domain.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TelegramService {

    // AIDEV-NOTE: Telegram API 메시지 최대 길이 제한
    private static final int MAX_MESSAGE_LENGTH = 4096;
    private static final int SAFE_MESSAGE_LENGTH = 4000; // 여유를 두고 4000자로 제한

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
        if (text.length() <= MAX_MESSAGE_LENGTH) {
            sendSingleMessage(text);
        } else {
            log.warn("메시지가 Telegram 제한({})을 초과하여 분할 전송합니다. 총 길이: {}", MAX_MESSAGE_LENGTH, text.length());
            sendLongMessage(text);
        }
    }

    private void sendSingleMessage(String text) {
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

    // AIDEV-NOTE: 긴 메시지를 여러 개로 분할하여 전송
    // 줄바꿈을 기준으로 분할하여 MarkdownV2 포맷이 깨지지 않도록 함
    private void sendLongMessage(String text) {
        List<String> chunks = splitMessageByLines(text, SAFE_MESSAGE_LENGTH);

        for (int i = 0; i < chunks.size(); i++) {
            String chunk = chunks.get(i);
            if (chunks.size() > 1) {
                // 여러 파트로 나뉠 경우 파트 번호 표시
                chunk = String.format("\\[Part %d/%d\\]\n\n%s", i + 1, chunks.size(), chunk);
            }
            sendSingleMessage(chunk);

            // API Rate Limit 방지를 위해 메시지 사이에 짧은 딜레이
            if (i < chunks.size() - 1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("메시지 전송 딜레이 중 인터럽트 발생", e);
                }
            }
        }
    }

    private List<String> splitMessageByLines(String text, int maxLength) {
        List<String> chunks = new ArrayList<>();
        String[] lines = text.split("\n");

        StringBuilder currentChunk = new StringBuilder();

        for (String line : lines) {
            // 현재 청크에 다음 줄을 추가했을 때 길이 체크
            int nextLength = currentChunk.length() + line.length() + 1; // +1 for \n

            if (nextLength > maxLength && currentChunk.length() > 0) {
                // 현재 청크가 꽉 찼으면 저장하고 새로운 청크 시작
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
            }

            // 단일 라인이 maxLength를 초과하는 경우
            if (line.length() > maxLength) {
                // 현재 청크를 먼저 저장
                if (currentChunk.length() > 0) {
                    chunks.add(currentChunk.toString());
                    currentChunk = new StringBuilder();
                }
                // 긴 라인을 강제로 분할
                chunks.addAll(splitLongLine(line, maxLength));
            } else {
                if (currentChunk.length() > 0) {
                    currentChunk.append("\n");
                }
                currentChunk.append(line);
            }
        }

        // 마지막 청크 추가
        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString());
        }

        return chunks;
    }

    private List<String> splitLongLine(String line, int maxLength) {
        List<String> parts = new ArrayList<>();
        int start = 0;

        while (start < line.length()) {
            int end = Math.min(start + maxLength, line.length());
            parts.add(line.substring(start, end));
            start = end;
        }

        return parts;
    }
}