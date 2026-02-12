package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.AdminUpdateLeaveRequest;
import com.bmilab.backend.domain.leave.dto.request.RejectLeaveRequest;
import com.bmilab.backend.domain.leave.dto.response.LeaveFindAllResponse;
import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.leave.service.LeaveService;
import com.bmilab.backend.global.annotation.OnlyAdmin;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@OnlyAdmin
@RestController
@RequestMapping("/admin/leaves")
@RequiredArgsConstructor
public class AdminLeaveController implements AdminLeaveApi {
    private final LeaveService leaveService;

    @PostMapping("/{leaveId}/approve")
    public ResponseEntity<Void> approveLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable long leaveId
    ) {
        leaveService.approveLeave(userAuthInfo.getUserId(), leaveId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{leaveId}/reject")
    public ResponseEntity<Void> rejectLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable long leaveId,
            @RequestBody @Valid RejectLeaveRequest request
    ) {
        leaveService.rejectLeave(userAuthInfo.getUserId(), leaveId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<LeaveFindAllResponse> getLeaves(
            @RequestParam(required = false) LeaveStatus status,
            @PageableDefault(size = 10, sort = "applicatedAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(leaveService.getLeavesByAdmin(status, pageable));
    }

    @PatchMapping("/{leaveId}")
    public ResponseEntity<Void> updateLeave(
            @PathVariable long leaveId,
            @RequestBody @Valid AdminUpdateLeaveRequest request
    ) {
        leaveService.updateLeaveByAdmin(leaveId, request);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{leaveId}")
    public ResponseEntity<Void> deleteLeave(@PathVariable long leaveId) {
        leaveService.deleteLeaveByAdmin(leaveId);
        return ResponseEntity.ok().build();
    }
}
