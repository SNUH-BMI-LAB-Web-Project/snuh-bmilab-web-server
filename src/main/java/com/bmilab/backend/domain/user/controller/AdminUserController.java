package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.RegisterUserRequest;
import com.bmilab.backend.domain.user.dto.request.AdminUpdateUserRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserStatusRequest;
import com.bmilab.backend.domain.user.dto.request.UserAccountEmailRequest;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.service.AuthService;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.annotation.OnlyAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OnlyAdmin
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController implements AdminUserApi {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> registerNewUser(@RequestBody @Valid RegisterUserRequest request) {

        authService.registerNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetail> getUserById(@PathVariable Long userId) {

        return ResponseEntity.ok(userService.getUserDetailById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(
            @PathVariable Long userId,
            @RequestBody @Valid AdminUpdateUserRequest request
    ) {

        userService.updateUserById(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {

        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/account-email")
    public ResponseEntity<Void> sendAccountEmail(
            @PathVariable Long userId,
            @RequestBody @Valid UserAccountEmailRequest request
    ) {

        userService.sendAccountEmail(userId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/resign")
    public ResponseEntity<Void> resignUser(@PathVariable Long userId) {

        userService.resignUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserStatusRequest request
    ) {

        userService.updateUserStatus(userId, request.status());
        return ResponseEntity.ok().build();
    }
}
