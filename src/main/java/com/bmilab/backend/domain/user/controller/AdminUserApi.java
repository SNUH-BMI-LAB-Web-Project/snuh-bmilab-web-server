package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.RegisterUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "(Admin) Auth", description = "관리자용 인증 처리 API")
public interface AdminUserApi {
    @Operation(summary = "신규 사용자 등록", description = "신규 사용자를 등록하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "사용자 등록 성공"
                    )
            }
    )
    ResponseEntity<Void> registerNewUser(@RequestBody RegisterUserRequest request);
}
