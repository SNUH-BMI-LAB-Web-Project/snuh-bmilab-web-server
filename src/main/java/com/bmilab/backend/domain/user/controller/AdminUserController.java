package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.RegisterUserRequest;
import com.bmilab.backend.domain.user.service.AuthService;
import com.bmilab.backend.global.security.annotation.OnlyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@OnlyAdmin
public class AdminUserController implements AdminUserApi {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Void> registerNewUser(@RequestBody RegisterUserRequest request) {
        authService.registerNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
