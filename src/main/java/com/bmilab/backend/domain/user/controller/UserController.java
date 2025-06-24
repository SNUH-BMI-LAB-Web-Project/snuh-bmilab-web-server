package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.UserEducationRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserPasswordRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.SearchUserResponse;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/search")
    public ResponseEntity<SearchUserResponse> searchUsers(
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetail> getCurrentUser(
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

    @PatchMapping("/me/educations")
    public ResponseEntity<Void> addEducations(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody UserEducationRequest request
    ) {
        userService.addEducations(userAuthInfo.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/educations/{educationId}")
    public ResponseEntity<Void> deleteEducations(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long educationId
    ) {
        userService.deleteEducations(userAuthInfo.getUserId(), educationId);
        return ResponseEntity.ok().build();
    }
}
