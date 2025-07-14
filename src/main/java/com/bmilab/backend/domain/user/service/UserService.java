package com.bmilab.backend.domain.user.service;

import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.dto.request.FindPasswordEmailRequest;
import com.bmilab.backend.domain.user.dto.request.UserAccountEmailRequest;
import com.bmilab.backend.domain.user.dto.request.UserEducationRequest;
import com.bmilab.backend.domain.user.dto.request.AdminUpdateUserRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserPasswordRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.query.UserCondition;
import com.bmilab.backend.domain.user.dto.request.UserSubAffiliationRequest;
import com.bmilab.backend.domain.user.dto.response.SearchUserResponse;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserEducation;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.entity.UserProjectCategory;
import com.bmilab.backend.domain.user.entity.UserSubAffiliation;
import com.bmilab.backend.domain.user.event.UserEducationUpdateEvent;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserEducationRepository;
import com.bmilab.backend.domain.user.repository.UserInfoRepository;
import com.bmilab.backend.domain.user.repository.UserProjectCategoryRepository;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.domain.user.repository.UserSubAffiliationRepository;
import com.bmilab.backend.global.email.EmailSender;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final EmailSender emailSender;
    private final ApplicationEventPublisher eventPublisher;
    private final UserEducationRepository userEducationRepository;
    private final UserProjectCategoryRepository userProjectCategoryRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserSubAffiliationRepository userSubAffiliationRepository;

    public User findUserById(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public List<User> findAllUsersById(Iterable<Long> userIds) {

        return userRepository.findAllById(userIds);
    }

    public UserFindAllResponse getAllUsers(UserCondition condition) {

        log.info("filterBy={}, filterValue={}", condition.getFilterBy(), condition.getFilterValue());

        PageRequest pageRequest = PageRequest.of(
                condition.getPageNo(),
                condition.getSize(),
                Sort.by(Direction.DESC, condition.getCriteria())
        );

        Page<UserInfoQueryResult> results = userRepository.searchUserInfos(condition, pageRequest);

        return UserFindAllResponse.of(condition, results);
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
        User user = result.user();
        List<UserEducation> educations = userEducationRepository.findAllByUser(user);
        List<ProjectCategory> categories = userProjectCategoryRepository.findAllByUser(user)
                .stream()
                .map(UserProjectCategory::getCategory)
                .toList();
        List<UserSubAffiliation> subAffiliations = userSubAffiliationRepository.findAllByUser(user);

        return UserDetail.from(result, educations, categories, subAffiliations, includeComment);
    }

    @Transactional
    public void updateUserById(Long userId, AdminUpdateUserRequest request) {

        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        User user = result.user();
        String newEmail = request.email();

        if (!user.getEmail().equals(newEmail)) {
            validateEmailDuplicate(newEmail);
        }

        result.userInfo().updateComment(request.comment());
        result.userLeave().updateAnnualLeaveCount(request.annualLeaveCount());

        user.update(request.name(), newEmail, request.organization(), request.department(), request.position());
        user.updateRole(request.role());

        result.userInfo().update(request.seatNumber(), request.phoneNumber());

        updateCategories(user, request.newCategoryIds(), request.deletedCategoryIds());
        updateSubAffiliations(user, request.subAffiliations());
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

        User user = result.user();
        if (profileImage != null) {
            String profileImageUrl = uploadProfileImage(userId, profileImage);

            user.updateProfileImageUrl(profileImageUrl);
        }

        user
                .update(
                        request.name(),
                        request.email(),
                        request.organization(),
                        request.department(),
                        request.position()

                );

        result.userInfo().update(request.seatNumber(), request.phoneNumber());

        updateCategories(user, request.newCategoryIds(), request.deletedCategoryIds());
        updateSubAffiliations(user, request.subAffiliations());
    }

    private void updateCategories(User user, List<Long> newCategoryIds, List<Long> deletedCategoryIds) {

        saveUserCategories(user, newCategoryIds);
        deletedCategoryIds.forEach((deletedCategoryId) -> userProjectCategoryRepository.deleteByUserIdAndCategoryId(
                user.getId(),
                deletedCategoryId
        ));
    }

    private void updateSubAffiliations(User user, List<UserSubAffiliationRequest> userSubAffiliationRequests) {
        log.info("userSubAffiliationRequests={}", userSubAffiliationRequests);
        List<UserSubAffiliation> original = userSubAffiliationRepository.findAllByUser(user);
        log.info("original={}", original.stream().map(UserSubAffiliation::getId).toList());
        List<UserSubAffiliation> exists =
                userSubAffiliationRepository.findExistsAsEntity(user, userSubAffiliationRequests);
        log.info("exists={}", exists.stream().map(UserSubAffiliation::getId).toList());
        List<UserSubAffiliationRequest> nonExistsAsRequest = userSubAffiliationRequests.stream()
                .filter(it ->
                        exists.stream()
                                .noneMatch(exist ->
                                        it.organization().equals(exist.getOrganization()) &&
                                                it.department().equals(exist.getDepartment()) &&
                                                it.position().equals(exist.getPosition())
                                )
                )
                .toList();
        log.info("nonExists={}", nonExistsAsRequest.stream().map(UserSubAffiliationRequest::toString).toList());

        //새로 추가된 소속 등록하기
        nonExistsAsRequest.stream()
                .map(it -> UserSubAffiliation.builder()
                        .user(user)
                        .organization(it.organization())
                        .department(it.department())
                        .position(it.position())
                        .build())
                .forEach(userSubAffiliationRepository::save);

        //없어진 서브 소속 지우기
        original.stream()
                .filter(it -> exists.stream().noneMatch(exist -> exist.getId().equals(it.getId())))
                .forEach(userSubAffiliationRepository::delete);
    }

    public void saveUserCategories(User user, List<Long> newCategoryIds) {

        newCategoryIds.stream()
                .map((newCategoryId) -> UserProjectCategory.builder()
                        .user(user)
                        .category(ProjectCategory.builder().id(newCategoryId).build())
                        .build())
                .forEach(userProjectCategoryRepository::save);
    }

    public void saveUserInfo(
            User user,
            String seatNumber,
            String phoneNumber,
            LocalDate joinedAt
    ) {

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
                .map(it -> UserEducation.builder()
                        .user(user)
                        .title(it.title())
                        .startYearMonth(it.startYearMonth())
                        .endYearMonth(it.endYearMonth())
                        .status(it.status())
                        .type(it.type())
                        .build())
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
                .type(request.type())
                .build();

        userEducationRepository.save(userEducation);
        eventPublisher.publishEvent(new UserEducationUpdateEvent(userId));
    }

    @Transactional
    public void deleteEducations(Long userId, Long userEducationId) {

        userEducationRepository.deleteById(userEducationId);
        eventPublisher.publishEvent(new UserEducationUpdateEvent(userId));
    }

    public void sendAccountEmail(Long userId, UserAccountEmailRequest request) {

        User user = findUserById(userId);
        String email = user.getEmail();

        emailSender.sendAccountCreateEmailAsync(email, user.getName(), request.password());
    }

    @Transactional
    public void sendFindPasswordEmail(FindPasswordEmailRequest request) {

        String email = request.email();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        String newPassword = generateRandomPassword();

        user.updatePassword(passwordEncoder.encode(newPassword));

        emailSender.sendFindPasswordEmailAync(email, user.getName(), newPassword);
    }

    public void validateEmailDuplicate(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new ApiException(UserErrorCode.DUPLICATE_EMAIL);
        }
    }

    private String generateRandomPassword() {

        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        StringBuilder tempPw = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            tempPw.append(charSet[idx]);
        }

        return tempPw.toString();
    }

    @Transactional
    public void saveUserSubAffiliations(User user, List<UserSubAffiliationRequest> userSubAffiliationRequests) {
        userSubAffiliationRequests.stream()
                .map(it -> UserSubAffiliation.builder()
                        .user(user)
                        .organization(it.organization())
                        .department(it.department())
                        .position(it.position())
                        .build())
                .forEach(userSubAffiliationRepository::save);
    }
}
