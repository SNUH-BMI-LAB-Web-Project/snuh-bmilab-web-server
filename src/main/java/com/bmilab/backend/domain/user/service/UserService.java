package com.bmilab.backend.domain.user.service;

import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.dto.request.UserEducationRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserPasswordRequest;
import com.bmilab.backend.domain.user.dto.request.AdminUpdateUserRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.SearchUserResponse;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserEducation;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.entity.UserProjectCategory;
import com.bmilab.backend.domain.user.event.UserEducationUpdateEvent;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserEducationRepository;
import com.bmilab.backend.domain.user.repository.UserInfoRepository;
import com.bmilab.backend.domain.user.repository.UserProjectCategoryRepository;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;
    private final UserEducationRepository userEducationRepository;
    private final UserProjectCategoryRepository userProjectCategoryRepository;
    private final UserInfoRepository userInfoRepository;

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

    public UserDetail getUserDetailById(Long userId) {
        return getUserDetailById(userId, true);
    }

    public UserDetail getCurrentUser(Long userId) {
        return getUserDetailById(userId, false);
    }

    private UserDetail getUserDetailById(Long userId, boolean includeComment) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        List<UserEducation> educations = userEducationRepository.findAllByUser(result.user());
        List<ProjectCategory> categories = userProjectCategoryRepository.findAllByUser(result.user())
                .stream()
                .map(UserProjectCategory::getCategory)
                .toList();

        return UserDetail.from(result, educations, categories, includeComment);
    }

    @Transactional
    public void updateUserById(Long userId, AdminUpdateUserRequest request) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        result.userInfo().updateComment(request.comment());
        result.userLeave().updateAnnualLeaveCount(request.annualLeaveCount());

        result.user().update(
                request.name(),
                request.email(),
                request.organization(),
                request.department(),
                request.affiliation()
        );

        result.userInfo().update(
                request.seatNumber(),
                request.phoneNumber()
        );

        updateCategories(result.user(), request.newCategoryIds(), request.deletedCategoryIds());
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
                request.seatNumber(),
                request.phoneNumber()
        );

        updateCategories(result.user(), request.newCategoryIds(), request.deletedCategoryIds());
    }

    private void updateCategories(User user, List<Long> newCategoryIds, List<Long> deletedCategoryIds) {
        saveUserCategories(user, newCategoryIds);
        deletedCategoryIds.forEach(userProjectCategoryRepository::deleteById);
    }

    public void saveUserCategories(User user, List<Long> newCategoryIds) {
        newCategoryIds.stream().map((newCategoryId) ->
                        UserProjectCategory.builder()
                                .user(user)
                                .category(ProjectCategory.builder().id(newCategoryId).build())
                                .build()
                )
                .forEach(userProjectCategoryRepository::save);
    }

    public void saveUserInfo(User user, String seatNumber, String phoneNumber, LocalDate joinedAt) {
        UserInfo userInfo = UserInfo.builder()
                .user(user)
                .seatNumber(seatNumber)
                .phoneNumber(phoneNumber)
                .joinedAt(joinedAt)
                .build();

        userInfoRepository.save(userInfo);
    }

    public void saveUserEducations(User user, List<UserEducationRequest> educationRequests) {
        educationRequests.stream()
                .map(it ->
                        UserEducation.builder()
                                .user(user)
                                .title(it.title())
                                .startYearMonth(it.startYearMonth())
                                .endYearMonth(it.endYearMonth())
                                .status(it.status())
                                .build()
                )
                .forEach(userEducationRepository::save);
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

    @Transactional
    public void addEducations(Long userId, UserEducationRequest request) {
        User user = findUserById(userId);
        UserEducation userEducation = UserEducation.builder()
                .user(user)
                .title(request.title())
                .startYearMonth(request.startYearMonth())
                .endYearMonth(request.endYearMonth())
                .status(request.status())
                .build();

        userEducationRepository.save(userEducation);
        eventPublisher.publishEvent(new UserEducationUpdateEvent(userId));
    }

    @Transactional
    public void deleteEducations(Long userId, Long userEducationId) {
        userEducationRepository.deleteById(userEducationId);
        eventPublisher.publishEvent(new UserEducationUpdateEvent(userId));
    }
}
