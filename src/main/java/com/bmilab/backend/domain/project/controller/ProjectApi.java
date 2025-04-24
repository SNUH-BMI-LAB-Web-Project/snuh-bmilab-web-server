package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.ProjectCompleteRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectFileRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectRequest;
import com.bmilab.backend.domain.project.dto.response.ProjectDetail;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse;
import com.bmilab.backend.global.annotation.FormDataRequestBody;
import com.bmilab.backend.global.exception.ErrorResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Project", description = "연구/프로젝트 API")
public interface ProjectApi {
    @Operation(summary = "신규 프로젝트 생성", description = "신규 프로젝트를 생성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "신규 프로젝트 생성 성공"
                    ),
            }
    )
    @FormDataRequestBody
    ResponseEntity<Void> createNewProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestPart List<MultipartFile> files,
            @RequestPart ProjectRequest request
    );

    @Operation(summary = "프로젝트 첨부파일 추가", description = "프로젝트에 첨부파일을 추가하는 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "첨부파일 추가 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "프로젝트 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "프로젝트에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> addProjectFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestPart MultipartFile file
    );

    @Operation(summary = "프로젝트 첨부파일 삭제", description = "프로젝트에서 첨부파일을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "첨부파일 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "프로젝트 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "프로젝트에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteProjectFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody ProjectFileRequest request
    );

    @Operation(summary = "프로젝트 수정", description = "프로젝트 정보를 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "프로젝트 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "프로젝트에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody ProjectRequest request
    );

    @Operation(summary = "프로젝트 종료 처리", description = "프로젝트 상태를 종료로 처리하는 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 종료 처리 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "프로젝트 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "프로젝트에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> completeProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody ProjectCompleteRequest request
    );

    @Operation(summary = "모든 프로젝트 조회", description = "모든 프로젝트를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모든 프로젝트 조회 성공"
                    )
            }
    )
    ResponseEntity<ProjectFindAllResponse> getAllProjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(summary = "프로젝트 상세 조회", description = "ID로 프로젝트를 상세 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 조회 성공"
                    )
            }
    )
    ResponseEntity<ProjectDetail> getProjectById(@PathVariable Long projectId);

    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "프로젝트 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "프로젝트에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteProjectById(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId
    );
}
