package com.bmilab.backend.domain.research.award.controller;

import com.bmilab.backend.domain.research.award.dto.request.CreateAwardRequest;
import com.bmilab.backend.domain.research.award.dto.request.UpdateAwardRequest;
import com.bmilab.backend.domain.research.award.dto.response.AwardFindAllResponse;
import com.bmilab.backend.domain.research.award.dto.response.AwardResponse;
import com.bmilab.backend.domain.research.award.service.AwardService;
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
@RequestMapping("/research/awards")
@RequiredArgsConstructor
public class AwardController implements AwardApi {

    private final AwardService awardService;

    @PostMapping
    public ResponseEntity<AwardResponse> createAward(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateAwardRequest request
    ) {
        AwardResponse response = awardService.createAward(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{awardId}")
    public ResponseEntity<AwardResponse> updateAward(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long awardId,
            @RequestBody @Valid UpdateAwardRequest request
    ) {
        AwardResponse response = awardService.updateAward(awardId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{awardId}")
    public ResponseEntity<AwardResponse> getAward(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long awardId
    ) {
        return ResponseEntity.ok(awardService.getAward(awardId));
    }

    @GetMapping
    public ResponseEntity<AwardFindAllResponse> getAwards(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "awardDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(awardService.getAwards(keyword, pageable));
    }

    @DeleteMapping("/{awardId}")
    public ResponseEntity<Void> deleteAward(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long awardId
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        awardService.deleteAward(userAuthInfo.getUserId(), isAdmin, awardId);
        return ResponseEntity.ok().build();
    }
}
