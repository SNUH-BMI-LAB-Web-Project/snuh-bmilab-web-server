package com.bmilab.backend.domain.user.service;

import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.repository.LeaveRepository;
import com.bmilab.backend.domain.leave.repository.UserLeaveRepository;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.CurrentUserDetail;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserInfoRepository;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserLeaveRepository userLeaveRepository;
    private final LeaveRepository leaveRepository;
    private final UserInfoRepository userInfoRepository;

    public UserFindAllResponse getAllUsers(int pageNo, String criteria) {
        PageRequest pageRequest = PageRequest.of(pageNo, 10, Sort.by(Direction.DESC, criteria));

        List<UserSummary> userSummaries = userRepository.findAll(pageRequest)
                .stream()
                .map(UserSummary::from)
                .toList();

        return new UserFindAllResponse(userSummaries);
    }

    public UserDetail getUserById(long userId) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        return UserDetail.from(result);
    }


    public CurrentUserDetail getCurrentUser(Long userId) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        List<Leave> leaves = leaveRepository.findAllByUserId(userId);

        return CurrentUserDetail.from(result, leaves);
    }

    @Transactional
    public void updateUserById(Long userId, UpdateUserRequest request) {
        UserDetailQueryResult result = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));


        result.userInfo().updateComment(request.comment());
        result.userLeave().updateAnnualLeaveCount(request.annualLeaveCount());
    }
}
