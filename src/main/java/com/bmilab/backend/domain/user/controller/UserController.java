package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.CurrentUserDetail;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserFindAllResponse> getAllUsers(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria) {
        return ResponseEntity.ok(userService.getAllUsers(pageNo, criteria));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetail> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        userService.updateUserById(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserDetail> getCurrentUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {
        return ResponseEntity.ok(userService.getCurrentUser(userAuthInfo.getUserId()));
    }
}
