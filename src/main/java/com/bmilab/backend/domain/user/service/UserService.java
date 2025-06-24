package com.bmilab.backend.domain.user.service;

import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.dto.request.UpdateUserPasswordRequest;
import com.bmilab.backend.domain.user.dto.request.AdminUpdateUserRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.CurrentUserDetail;
import com.bmilab.backend.domain.user.dto.response.SearchUserResponse;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public List<User> findAllUsersById(Iterable<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    public UserFindAllResponse getAllUsers(int pageNo, String criteria) {
        PageRequest pageRequest = PageRequest.of(pageNo, 10, Sort.by(Direction.DESC, criteria));

        Page<UserInfoQueryResult> results = userRepository.findAllUserInfosPagination(pageRequest);

        return UserFindAllResponse.of(results);
    }

    public UserDetail getUserDetailById(long userId) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        return UserDetail.from(result, true);
    }

    public CurrentUserDetail getCurrentUser(Long userId) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        return CurrentUserDetail.from(result, result.leaves());
    }

    @Transactional
    public void updateUserById(Long userId, AdminUpdateUserRequest request) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        result.userInfo().updateComment(request.comment());
        result.userLeave().updateAnnualLeaveCount(request.annualLeaveCount());
    }

    @Transactional
    public void updatePassword(Long userId, UpdateUserPasswordRequest request) {
        User user = findUserById(userId);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new ApiException(UserErrorCode.PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new ApiException(UserErrorCode.SAME_AS_CURRENT_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public void updateCurrentUser(Long userId, MultipartFile profileImage, UpdateUserRequest request) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        if (profileImage != null) {
            String profileImageUrl = uploadProfileImage(userId, profileImage);

            result.user().updateProfileImageUrl(profileImageUrl);
        }

        result.user().update(
                request.name(),
                request.email(),
                request.organization(),
                request.department(),
                request.affiliation()
        );

        result.userInfo().update(
                request.categories(),
                request.seatNumber(),
                request.phoneNumber(),
                request.education()
        );
    }

    private String uploadProfileImage(Long userId, MultipartFile file) {
        String contentType = Objects.requireNonNull(file.getContentType());

        if (!contentType.contains("image")) {
            throw new ApiException(UserErrorCode.INVALID_PROFILE_IMAGE_FILE_TYPE);
        }

        String newFileDir = "profiles/" + userId;

        return s3Service.uploadFile(file, newFileDir);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public SearchUserResponse searchUsers(String keyword) {
        List<User> users = userRepository.searchUsersByKeyword(keyword);

        return SearchUserResponse.of(users);
    }
}
