package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.ApplyLeaveRequest;
import com.bmilab.backend.domain.leave.dto.response.LeaveFindAllResponse;
import com.bmilab.backend.domain.leave.service.LeaveService;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/leaves")
@RequiredArgsConstructor
public class LeaveController implements LeaveApi {
    private final LeaveService leaveService;

    @GetMapping
    public ResponseEntity<LeaveFindAllResponse> getLeaves(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(leaveService.getLeaves(startDate, endDate));
    }

    @GetMapping("/me")
    public ResponseEntity<LeaveFindAllResponse> getLeavesByUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {
        return ResponseEntity.ok(leaveService.getLeavesByUser(userAuthInfo.getUserId()));
    }

    @PostMapping
    public ResponseEntity<Void> applyLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid ApplyLeaveRequest request
    ) {
        leaveService.applyLeave(userAuthInfo.getUserId(), request);
        return ResponseEntity.ok().build();
    }
}
