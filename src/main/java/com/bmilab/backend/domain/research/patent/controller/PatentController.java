package com.bmilab.backend.domain.research.patent.controller;

import com.bmilab.backend.domain.research.patent.dto.request.CreatePatentRequest;
import com.bmilab.backend.domain.research.patent.dto.request.UpdatePatentRequest;
import com.bmilab.backend.domain.research.patent.dto.response.PatentFindAllResponse;
import com.bmilab.backend.domain.research.patent.dto.response.PatentResponse;
import com.bmilab.backend.domain.research.patent.service.PatentService;
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
@RequestMapping("/research/patents")
@RequiredArgsConstructor
public class PatentController implements PatentApi {

    private final PatentService patentService;

    @PostMapping
    public ResponseEntity<PatentResponse> createPatent(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreatePatentRequest request
    ) {
        PatentResponse response = patentService.createPatent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{patentId}")
    public ResponseEntity<PatentResponse> updatePatent(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long patentId,
            @RequestBody @Valid UpdatePatentRequest request
    ) {
        PatentResponse response = patentService.updatePatent(patentId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{patentId}")
    public ResponseEntity<PatentResponse> getPatent(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long patentId
    ) {
        return ResponseEntity.ok(patentService.getPatent(patentId));
    }

    @GetMapping
    public ResponseEntity<PatentFindAllResponse> getPatents(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "applicationDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(patentService.getPatents(keyword, pageable));
    }

    @DeleteMapping("/{patentId}")
    public ResponseEntity<Void> deletePatent(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long patentId
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        patentService.deletePatent(userAuthInfo.getUserId(), isAdmin, patentId);
        return ResponseEntity.ok().build();
    }
}
