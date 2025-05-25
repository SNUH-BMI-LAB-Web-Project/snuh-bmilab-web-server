package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.TimelineRequest;
import com.bmilab.backend.domain.project.dto.response.TimelineFindAllResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Timeline", description = "연구 타임라인 기록 API")
public interface TimelineApi {
    @Operation(summary = "연구에 타임라인 기록 생성", description = "연구에 타임라인 기록을 추가 생성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "타임라인 기록 생성 성공"
                    )
            }
    )
    ResponseEntity<Void> createTimeline(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody TimelineRequest request
    );

    @Operation(summary = "연구의 타임라인 기록 전체 조회", description = "연구의 타임라인 기록을 전체 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "타임라인 기록 조회 성공"
                    )
            }
    )
    ResponseEntity<TimelineFindAllResponse> getAllTimelinesByProjectId(
            @PathVariable Long projectId
    );

    @Operation(summary = "타임라인 첨부파일 삭제", description = "타임라인 첨부파일을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "타임라인 첨부파일 삭제 성공"
                    )
            }
    )
    ResponseEntity<Void> deleteTimelineFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @PathVariable Long timelineId,
            @PathVariable UUID fileId
    );

    @Operation(summary = "타임라인 수정", description = "연구 타임라인을 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "타임라인 수정 성공"
                    )
            }
    )
    ResponseEntity<Void> updateTimeline(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @PathVariable Long timelineId,
            @RequestBody TimelineRequest request
    );

    @Operation(summary = "타임라인 삭제", description = "연구 타임라인과 관련 첨부파일을 모두 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "타임라인 삭제 성공"
                    )
            }
    )
    ResponseEntity<Void> deleteTimeline(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @PathVariable Long timelineId
    );
}
