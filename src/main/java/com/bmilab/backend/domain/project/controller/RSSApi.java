package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.response.RSSResponse;
import com.bmilab.backend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "NTIS RSS", description = "NTIS R&D 과제 공고 RSS 조회 API")
public interface RSSApi {
    @Operation(summary = "RSS 데이터 조회", description = "RSS 데이터를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "RSS 데이터 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "유효하지 않은 RSS 응답 데이터를 받았습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<RSSResponse> getAllRssAssignments(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int size
    );
}
