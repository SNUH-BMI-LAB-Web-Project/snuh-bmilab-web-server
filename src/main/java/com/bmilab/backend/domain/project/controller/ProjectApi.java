package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.ProjectCompleteRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectRequest;
import com.bmilab.backend.domain.project.dto.response.ProjectDetail;
import com.bmilab.backend.domain.project.dto.response.ProjectFileFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse;
import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.global.exception.ErrorResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Project", description = "연구 API")
public interface ProjectApi {
    @Operation(summary = "신규 연구 생성", description = "신규 연구 데이터를 생성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "신규 연구 데이터 생성 성공"
                    ),
            }
    )
    ResponseEntity<Void> createNewProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody ProjectRequest request
    );

    @Operation(summary = "연구 첨부파일 삭제", description = "연구 데이터에서 첨부파일을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "첨부파일 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "연구 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "연구에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteProjectFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @PathVariable UUID fileId
    );

    @Operation(summary = "연구 자료실 첨부파일 조회", description = "연구 자료실을 위한 첨부파일 목록을 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "첨부파일 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "연구 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<ProjectFileFindAllResponse> getAllProjectFiles(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId
    );

    @Operation(summary = "연구 수정", description = "연구 정보를 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "연구 정보 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "연구 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "연구에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody ProjectRequest request
    );

    @Operation(summary = "연구 종료 처리", description = "연구 상태를 종료로 처리하는 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "연구 종료 처리 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "연구 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "연구에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> completeProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody ProjectCompleteRequest request
    );

    @Operation(summary = "연구와 연관된 일일보고 목록 조회", description = "연구와 연관된 일일보고 목록을 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일일보고 목록 조회 성공"
                    )
            }
    )
    ResponseEntity<ReportFindAllResponse> getReportsByProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    );

    @Operation(summary = "모든 연구 조회", description = "모든 연구를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모든 연구 조회 성공"
                    )
            }
    )
    @PageableAsQueryParam
    ResponseEntity<ProjectFindAllResponse> getAllProjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long leaderId,
            @RequestParam(required = false) ProjectCategory category,
            @RequestParam(required = false) ProjectStatus status,
            @PageableDefault(sort = "createdAt", direction = Direction.DESC) @ParameterObject Pageable pageable
    );

    @Operation(summary = "연구 상세 조회", description = "ID로 연구를 상세 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "연구 조회 성공"
                    )
            }
    )
    ResponseEntity<ProjectDetail> getProjectById(@PathVariable Long projectId);

    @Operation(summary = "연구 삭제", description = "연구 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "연구 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "연구 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "연구에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteProjectById(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId
    );
}
