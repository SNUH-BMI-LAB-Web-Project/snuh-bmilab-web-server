package com.bmilab.backend.domain.projectcategory.controller;

import com.bmilab.backend.domain.projectcategory.dto.request.ProjectCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "(Admin) Project Category", description = "(관리자용) 연구 분야 API")
public interface AdminProjectCategoryApi {

    @Operation(summary = "연구 분야 추가", description = "새로운 연구 분야를 추가하기 위한 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "연구 분야 추가 성공"
                    ),
            }
    )
    ResponseEntity<Void> createProjectCategory(@RequestBody ProjectCategoryRequest request);

    @Operation(summary = "연구 분야 수정", description = "기존 연구 분야를 수정하기 위한 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전체 연구 분야 조회 성공"
                    ),
            }
    )
    ResponseEntity<Void> updateProjectCategory(
            @PathVariable Long categoryId,
            @RequestBody ProjectCategoryRequest request
    );

    @Operation(summary = "연구 분야 삭제", description = "연구 분야를 삭제하기 위한 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "연구 분야 삭제 성공"
                    ),
            }
    )
    ResponseEntity<Void> deleteById(@PathVariable Long categoryId);
}
