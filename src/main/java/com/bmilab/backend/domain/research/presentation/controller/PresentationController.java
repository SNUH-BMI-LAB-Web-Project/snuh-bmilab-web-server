package com.bmilab.backend.domain.research.presentation.controller;

import com.bmilab.backend.domain.research.presentation.dto.request.CreateAcademicPresentationRequest;
import com.bmilab.backend.domain.research.presentation.dto.request.UpdateAcademicPresentationRequest;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationFindAllResponse;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationResponse;
import com.bmilab.backend.domain.research.presentation.service.PresentationService;
import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/research/academic-presentations")
@RequiredArgsConstructor
public class PresentationController implements PresentationApi {

    private final PresentationService presentationService;

    @PostMapping
    public ResponseEntity<AcademicPresentationResponse> createAcademicPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateAcademicPresentationRequest request
    ) {
        AcademicPresentationResponse response = presentationService.createAcademicPresentation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{academicPresentationId}")
    public ResponseEntity<AcademicPresentationResponse> updateAcademicPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long academicPresentationId,
            @RequestBody @Valid UpdateAcademicPresentationRequest request
    ) {
        AcademicPresentationResponse response = presentationService.updateAcademicPresentation(academicPresentationId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{academicPresentationId}")
    public ResponseEntity<AcademicPresentationResponse> getAcademicPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long academicPresentationId
    ) {
        return ResponseEntity.ok(presentationService.getAcademicPresentation(academicPresentationId));
    }

    @GetMapping
    public ResponseEntity<AcademicPresentationFindAllResponse> getAcademicPresentations(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "academicPresentationStartDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(presentationService.getAcademicPresentations(keyword, pageable));
    }

    @DeleteMapping("/{academicPresentationId}")
    public ResponseEntity<Void> deleteAcademicPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long academicPresentationId
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        presentationService.deleteAcademicPresentation(userAuthInfo.getUserId(), isAdmin, academicPresentationId);
        return ResponseEntity.ok().build();
    }
}
