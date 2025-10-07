package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record TaskProposalUpdateRequest(

        @Schema(description = "제안서 제출 마감일", example = "2025-02-28T18:00:00")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime proposalDeadline,

        @Schema(description = "제안서 작성 연구원 ID 목록", example = "[101, 102, 103]")
        List<Long> proposalWriterIds,

        @Schema(description = "발주처 담당자 이름", example = "홍길동")
        String contractorContactName,

        @Schema(description = "발주처 담당자 부서", example = "산업기술혁신과")
        String contractorContactDepartment,

        @Schema(description = "발주처 담당자 이메일", example = "hong@example.com")
        String contractorContactEmail,

        @Schema(description = "발주처 담당자 전화번호", example = "02-1234-5678")
        String contractorContactPhone,

        @Schema(description = "원내 담당자 이름", example = "김철수")
        String internalContactName,

        @Schema(description = "원내 담당자 부서", example = "연구지원팀")
        String internalContactDepartment,

        @Schema(description = "원내 담당자 이메일", example = "kimcs@snuh.ac.kr")
        String internalContactEmail,

        @Schema(description = "원내 담당자 전화번호", example = "010-9876-5432")
        String internalContactPhone
) {

}