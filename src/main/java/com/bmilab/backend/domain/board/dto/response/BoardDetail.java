package com.bmilab.backend.domain.board.dto.response;

import com.bmilab.backend.domain.board.entity.Board;
import com.bmilab.backend.domain.board.entity.BoardFile;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BoardDetail(
        @Schema(description = "게시판 ID", example = "1")
        Long boardId,

        @Schema(description = "작성자 정보")
        UserSummary author,

        @Schema(description = "게시판 분야")
        BoardCategorySummary boardCategory,

        @Schema(description = "게시판 제목", example = "신입 연구원 근무 수칙 안내")
        String title,

        @Schema(description = "게시판 내용", example = "신입 연구원 여러분, 환영합니다.")
        String content,

        @Schema(description = "조회수", example = "1")
        Integer viewCount,

        @Schema(description = "첨부된 파일 정보 목록")
        List<BoardFileSummary> files,

        @Schema(description = "게시글 생성 시각", example = "2025-07-19T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "게시글 수정 시각", example = "2025-07-19T10:30:00")
        LocalDateTime updatedAt
) {
        public static BoardDetail from(Board board, List<BoardFile> boardFiles) {
                return BoardDetail
                        .builder()
                        .boardId(board.getId())
                        .author(UserSummary.from(board.getAuthor()))
                        .boardCategory(BoardCategorySummary.from(board.getCategory()))
                        .title(board.getTitle())
                        .content(board.getContent())
                        .viewCount(board.getViewCount())
                        .files(boardFiles.stream()
                                .map(BoardFileSummary::from)
                                .collect(Collectors.toList()))
                        .build();

        }
}
