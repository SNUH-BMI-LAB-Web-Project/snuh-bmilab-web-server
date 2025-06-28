package com.bmilab.backend.domain.projectcategory.controller;

import com.bmilab.backend.domain.projectcategory.dto.response.ProjectCategoryFindAllResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Project Category", description = "연구 분야 API")
public interface ProjectCategoryApi {
    @Operation(summary = "전체 연구 분야 조회", description = "전체 연구 분야 목록을 조회하기 위한 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전체 연구 분야 조회 성공"
                    ),
            }
    )
    ResponseEntity<ProjectCategoryFindAllResponse> getAllProjectCategories();
}
