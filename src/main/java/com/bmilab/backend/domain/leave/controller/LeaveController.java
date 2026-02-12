package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.ApplyLeaveRequest;
import com.bmilab.backend.domain.leave.dto.response.LeaveFindAllResponse;
import com.bmilab.backend.domain.leave.dto.response.UserLeaveResponse;
import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.leave.service.LeaveService;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/leaves")
@RequiredArgsConstructor
public class LeaveController implements LeaveApi {
    private final LeaveService leaveService;

    @GetMapping
    public ResponseEntity<LeaveFindAllResponse> getLeaves(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        return ResponseEntity.ok(leaveService.getLeaves(startDate, endDate, LeaveStatus.APPROVED));
    }

    @GetMapping("/me")
    public ResponseEntity<UserLeaveResponse> getLeavesByUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PageableDefault(size = 10, sort = "applicatedAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(leaveService.getLeavesByUser(userAuthInfo.getUserId(), pageable));
    }

    @PostMapping
    public ResponseEntity<Void> applyLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid ApplyLeaveRequest request
    ) {
        leaveService.applyLeave(userAuthInfo.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{leaveId}")
    public ResponseEntity<Void> cancelLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable long leaveId
    ) {
        leaveService.cancelLeaveByUser(userAuthInfo.getUserId(), leaveId);
        return ResponseEntity.ok().build();
    }
}
