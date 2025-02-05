package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.ApproveSignupRequest;
import com.bmilab.backend.domain.user.dto.response.SignupRequestFindAllResponse;
import com.bmilab.backend.domain.user.service.AuthService;
import com.bmilab.backend.global.security.annotation.OnlyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sign-up")
@RequiredArgsConstructor
@OnlyAdmin
public class AdminSignupController implements AdminSignupApi {
    private final AuthService authService;

    @GetMapping("/request")
    public ResponseEntity<SignupRequestFindAllResponse> getAllSignupRequests(
            @RequestParam boolean isPending
    ) {
        return ResponseEntity.ok(authService.getAllSignupRequests(isPending));
    }

    @PostMapping("/approve")
    public ResponseEntity<Void> approveSignup(@RequestBody ApproveSignupRequest request) {
        authService.approveSignup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<Void> rejectSignup(@PathVariable Long requestId) {
        authService.rejectSignup(requestId);
        return ResponseEntity.ok().build();
    }
}
