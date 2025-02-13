package com.bmilab.backend.domain.user.service;

import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.leave.exception.LeaveErrorCode;
import com.bmilab.backend.domain.leave.repository.LeaveRepository;
import com.bmilab.backend.domain.leave.repository.UserLeaveRepository;
import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.CurrentUserDetail;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
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

    public UserFindAllResponse getAllUsers(int pageNo, String criteria) {
        PageRequest pageRequest = PageRequest.of(pageNo, 10, Sort.by(Direction.DESC, criteria));

        List<UserSummary> userSummaries = userRepository.findAll(pageRequest)
                .stream()
                .map(UserSummary::from)
                .toList();

        return new UserFindAllResponse(userSummaries);
    }

    public UserDetail getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        UserLeave userLeave = userLeaveRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(LeaveErrorCode.USER_LEAVE_NOT_FOUND));

        return UserDetail.from(user, userLeave);
    }


    public CurrentUserDetail getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        UserLeave userLeave = userLeaveRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(LeaveErrorCode.USER_LEAVE_NOT_FOUND));

        List<Leave> leaves = leaveRepository.findAllByUserId(userId);

        return CurrentUserDetail.from(user, userLeave, leaves);
    }

    @Transactional
    public void updateUserById(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        UserLeave userLeave = userLeaveRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(LeaveErrorCode.USER_LEAVE_NOT_FOUND));

        user.updateComment(request.comment());
        userLeave.updateAnnualLeaveCount(request.annualLeaveCount());
    }
}
