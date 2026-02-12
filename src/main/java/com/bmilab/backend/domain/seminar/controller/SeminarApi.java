package com.bmilab.backend.domain.seminar.controller;

import com.bmilab.backend.domain.seminar.dto.request.CreateSeminarRequest;
import com.bmilab.backend.domain.seminar.dto.request.UpdateSeminarRequest;
import com.bmilab.backend.domain.seminar.dto.response.SeminarFindAllResponse;
import com.bmilab.backend.domain.seminar.dto.response.SeminarResponse;
import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Seminar", description = "세미나/학회 캘린더 API")
public interface SeminarApi {

    @Operation(summary = "날짜별 세미나/학회 조회", description = "기간 내 세미나/학회 일정을 조회하는 GET API (캘린더 뷰)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<SeminarFindAllResponse> getSeminarsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    );

    @Operation(summary = "세미나/학회 검색", description = "키워드와 라벨로 세미나/학회를 검색하는 GET API (리스트 뷰)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<SeminarFindAllResponse> searchSeminars(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) SeminarLabel label,
            @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "세미나/학회 상세 조회", description = "세미나/학회 상세 정보를 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "세미나/학회 일정을 찾을 수 없습니다.")
    })
    ResponseEntity<SeminarResponse> getSeminarById(@PathVariable Long seminarId);

    @Operation(summary = "세미나/학회 일정 생성", description = "새로운 세미나/학회 일정을 생성하는 POST API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공")
    })
    ResponseEntity<Void> createSeminar(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody CreateSeminarRequest request
    );

    @Operation(summary = "세미나/학회 일정 수정", description = "세미나/학회 일정을 수정하는 PUT API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "403", description = "수정/삭제 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "세미나/학회 일정을 찾을 수 없습니다.")
    })
    ResponseEntity<Void> updateSeminar(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long seminarId,
            @RequestBody UpdateSeminarRequest request
    );

    @Operation(summary = "세미나/학회 일정 삭제", description = "세미나/학회 일정을 삭제하는 DELETE API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "수정/삭제 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "세미나/학회 일정을 찾을 수 없습니다.")
    })
    ResponseEntity<Void> deleteSeminar(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long seminarId
    );
}
