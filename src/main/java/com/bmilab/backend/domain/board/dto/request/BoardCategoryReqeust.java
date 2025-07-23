package com.bmilab.backend.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardCategoryReqeust(
        @Schema(description = "게시판 분야 이름", example = "공지사항")
        @NotBlank(message = "게시판 분야 이름은 필수입니다.")
        @Size(max = 30, message = "게시판 분야 이름은 30자 이하로 입력해주세요.")
        String name
) {
}
