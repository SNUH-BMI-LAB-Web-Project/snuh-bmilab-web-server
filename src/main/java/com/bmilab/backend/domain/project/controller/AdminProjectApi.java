package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.ExternalProfessorRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectPinRequest;
import com.bmilab.backend.domain.project.dto.response.ExternalProfessorFindAllResponse;
import com.bmilab.backend.global.exception.ErrorResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "(Admin) Project", description = "(관리자용) 프로젝트 관리 API")
public interface AdminProjectApi {

    @Operation(summary = "외부 교수 등록", description = "외부 교수 정보를 등록하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 교수 등록 성공"
                    )
            }
    )
    ResponseEntity<Void> createExternalProfessor(@RequestBody ExternalProfessorRequest request);

    @Operation(summary = "외부 교수 수정", description = "외부 교수 정보를 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 교수 정보 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "외부 교수 정보를 찾을 수 업습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateExternalProfessor(
            @PathVariable Long professorId,
            @RequestBody ExternalProfessorRequest request
    );

    @Operation(summary = "외부 교수 목록 조회", description = "등록된 외부 교수 목록을 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 교수 목록 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "외부 교수 정보를 찾을 수 업습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<ExternalProfessorFindAllResponse> getAllExternalProfessors();

    @Operation(summary = "외부 교수 삭제", description = "외부 교수 정보를 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 교수 정보 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "외부 교수 정보를 찾을 수 업습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteExternalProfessor(@PathVariable Long professorId);

    @Operation(summary = "연구 고정 상태 수정", description = "기존 연구 고정상태를 수정하기 위한 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "연구 고정 상태 수정 성공"),
            }
    )
    ResponseEntity<Void> updateProjectPinStatus(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectPinRequest request
    );
}
