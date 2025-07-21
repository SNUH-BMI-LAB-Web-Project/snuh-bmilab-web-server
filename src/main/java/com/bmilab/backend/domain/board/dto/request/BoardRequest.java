package com.bmilab.backend.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BoardRequest (
        @Schema(description = "게시판 분야 ID", example = "1.0")
        @NotNull
        Long boardCategoryId,

        @Schema(description = "게시판 제목", example = "신입 연구원 근무 수칙 안내")
        @NotBlank(message = "게시판 제목은 필수입니다.")
        String title,

        @Schema(description = "게시판 내용", example = "신입 연구원 여러분, 환영합니다.")
        @NotNull(message = "게시판 내용은 필수입니다.")
        String content,

        @Schema(description  = "일반 첨부파일 ID 리스트", example = "[\"dfaef-afaefaef-aefaefae-...\", \"dfaef-afaefaef-aefaefae-..., dfaef-afaefaef-aefaefae-...\"]")
        @Nullable
        List<UUID> fileIds
){
}
