package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.MeetingRequest;
import com.bmilab.backend.domain.project.dto.response.MeetingFindAllResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Meeting", description = "연구 미팅 기록 API")
public interface MeetingApi {
    @Operation(summary = "연구에 미팅 기록 생성", description = "연구에 미팅 기록을 추가 생성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "미팅 기록 생성 성공"
                    )
            }
    )
    ResponseEntity<Void> createMeeting(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody MeetingRequest request
    );

    @Operation(summary = "연구의 미팅 기록 전체 조회", description = "연구의 미팅 기록을 전체 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "미팅 기록 조회 성공"
                    )
            }
    )
    ResponseEntity<MeetingFindAllResponse> getAllMeetingsByProjectId(
            @PathVariable Long projectId
    );
}
