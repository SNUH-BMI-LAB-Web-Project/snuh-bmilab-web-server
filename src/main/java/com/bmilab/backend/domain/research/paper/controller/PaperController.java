package com.bmilab.backend.domain.research.paper.controller;

import com.bmilab.backend.domain.research.paper.dto.request.CreatePaperRequest;
import com.bmilab.backend.domain.research.paper.dto.request.UpdatePaperRequest;
import com.bmilab.backend.domain.research.paper.dto.response.PaperResponse;
import com.bmilab.backend.domain.research.paper.dto.response.PaperSummaryResponse;
import com.bmilab.backend.domain.research.paper.service.PaperService;
import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/research/papers")
@RequiredArgsConstructor
public class PaperController implements PaperApi {

    private final PaperService paperService;

    @PostMapping
    public ResponseEntity<PaperResponse> createPaper(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreatePaperRequest request
    ) {
        PaperResponse response = paperService.createPaper(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{paperId}")
    public ResponseEntity<PaperResponse> updatePaper(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long paperId,
            @RequestBody @Valid UpdatePaperRequest request
    ) {
        PaperResponse response = paperService.updatePaper(paperId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paperId}")
    public ResponseEntity<PaperResponse> getPaper(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long paperId
    ) {
        return ResponseEntity.ok(paperService.getPaper(paperId));
    }

    @GetMapping
    public ResponseEntity<Page<PaperSummaryResponse>> getPapers(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "acceptDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(paperService.getPapers(keyword, pageable));
    }

    @DeleteMapping("/{paperId}")
    public ResponseEntity<Void> deletePaper(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long paperId
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        paperService.deletePaper(userAuthInfo.getUserId(), isAdmin, paperId);
        return ResponseEntity.ok().build();
    }
}
