package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.ApproveSignupRequest;
import com.bmilab.backend.domain.user.dto.request.LoginRequest;
import com.bmilab.backend.domain.user.dto.request.SignupRequest;
import com.bmilab.backend.domain.user.dto.response.LoginResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/sign-up/request")
    public ResponseEntity<Void> requestSignup(@RequestBody SignupRequest request) {
        authService.requestSignup(request);
        return ResponseEntity.ok().build();
    }
}
