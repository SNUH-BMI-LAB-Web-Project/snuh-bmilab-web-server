package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.RejectLeaveRequest;
import com.bmilab.backend.domain.leave.service.LeaveService;
import com.bmilab.backend.global.security.annotation.OnlyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OnlyAdmin
@RestController
@RequestMapping("/admin/leaves")
@RequiredArgsConstructor
public class AdminLeaveController implements AdminLeaveApi {
    private final LeaveService leaveService;

    @PostMapping("/approve/{leaveId}")
    public ResponseEntity<Void> approveLeave(@PathVariable long leaveId) {
        leaveService.approveLeave(leaveId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{leaveId}")
    public ResponseEntity<Void> rejectLeave(
            @PathVariable long leaveId,
            @RequestBody RejectLeaveRequest request
    ) {
        leaveService.rejectLeave(leaveId, request);
        return ResponseEntity.ok().build();
    }
}
