package com.bmilab.backend.domain.user.service;

import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.leave.exception.LeaveErrorCode;
import com.bmilab.backend.domain.leave.repository.UserLeaveRepository;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserLeaveRepository userLeaveRepository;

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
}
