package com.bmilab.backend.domain.task.controller;

import com.bmilab.backend.domain.task.dto.request.TaskAgreementUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskBasicInfoUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPeriodUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPresentationUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskProposalUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskRequest;
import com.bmilab.backend.domain.task.dto.response.TaskAgreementResponse;
import com.bmilab.backend.domain.task.dto.response.TaskBasicInfoResponse;
import com.bmilab.backend.domain.task.dto.response.TaskBasicResponse;
import com.bmilab.backend.domain.task.dto.response.TaskPresentationResponse;
import com.bmilab.backend.domain.task.dto.response.TaskProposalResponse;
import com.bmilab.backend.domain.task.dto.response.TaskStatsResponse;
import com.bmilab.backend.domain.task.dto.response.TaskSummaryResponse;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.global.exception.ErrorResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Task", description = "과제 관리 API")
public interface TaskApi {

    @Operation(summary = "과제 생성", description = "새로운 과제를 생성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "과제 생성 성공"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "중복된 연구과제번호입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> createTask(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody TaskRequest request
    );

    @Operation(summary = "과제 수정", description = "과제 전체 정보를 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "과제 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "과제 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "수정할 수 없는 상태입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )

    ResponseEntity<Void> updateTask(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody TaskRequest request
    );

    @Operation(summary = "과제 통계 조회", description = "과제 통계 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "과제 통계 조회 성공"
                    )
            }
    )
    ResponseEntity<TaskStatsResponse> getTaskStats(@AuthenticationPrincipal UserAuthInfo userAuthInfo);

    @Operation(summary = "과제 목록 조회", description = "과제 목록을 페이징으로 조회하는 GET API. 필터링 및 검색 가능")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "과제 목록 조회 성공"
                    )
            }
    )
    ResponseEntity<Page<TaskSummaryResponse>> getAllTasks(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    );

    @Operation(summary = "과제 기본정보 조회", description = "과제 ID로 기본정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "기본정보 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "과제 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<TaskBasicInfoResponse> getTaskBasicInfo(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    );


    @Operation(summary = "과제 기본정보 수정", description = "과제의 기본 정보(소관부처, 전문기관 등)를 수정하는 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "과제 기본 정보 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "과제 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateBasicInfo(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody TaskBasicInfoUpdateRequest request
    );

    @Operation(summary = "과제 제안서 정보 조회", description = "과제 ID로 제안서 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "제안서 정보 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "과제 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<TaskProposalResponse> getTaskProposal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    );

    @Operation(summary = "제안서 정보 수정", description = "제안서 작성 관련 정보를 수정하는 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "제안서 정보 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "과제 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateProposal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody TaskProposalUpdateRequest request
    );
}
