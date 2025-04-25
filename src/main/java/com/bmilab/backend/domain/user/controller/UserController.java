package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.UpdateUserPasswordRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.CurrentUserDetail;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/me")
    public ResponseEntity<CurrentUserDetail> getCurrentUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {

        return ResponseEntity.ok(userService.getCurrentUser(userAuthInfo.getUserId()));
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCurrentUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestPart(required = false) MultipartFile profileImage,
            @RequestPart UpdateUserRequest request
    ) {

        userService.updateCurrentUser(userAuthInfo.getUserId(), profileImage, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody UpdateUserPasswordRequest request
    ) {

        userService.updatePassword(userAuthInfo.getUserId(), request);
        return ResponseEntity.ok().build();
    }
}
