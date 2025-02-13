package com.bmilab.backend.domain.leave.service;

import com.bmilab.backend.domain.leave.dto.request.ApplyLeaveRequest;
import com.bmilab.backend.domain.leave.dto.request.RejectLeaveRequest;
import com.bmilab.backend.domain.leave.dto.response.LeaveFindAllResponse;
import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.leave.enums.LeaveType;
import com.bmilab.backend.domain.leave.exception.LeaveErrorCode;
import com.bmilab.backend.domain.leave.repository.LeaveRepository;
import com.bmilab.backend.domain.leave.repository.UserLeaveRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeaveService {
    private final LeaveRepository leaveRepository;
    private final UserRepository userRepository;
    private final UserLeaveRepository userLeaveRepository;

    public LeaveFindAllResponse getLeaves(LocalDateTime startDate, LocalDateTime endDate) {
        List<Leave> leaves =
                (startDate != null && endDate != null) ?
                        leaveRepository.findAllByBetweenDates(startDate, endDate) :
                        leaveRepository.findAll();

        return LeaveFindAllResponse.of(leaves);
    }

    public LeaveFindAllResponse getLeavesByUser(Long userId) {
        List<Leave> leaves = leaveRepository.findAllByUserId(userId);

        return LeaveFindAllResponse.of(leaves);
    }

    @Transactional
    public void applyLeave(Long userId, ApplyLeaveRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        double leaveCount = (request.endDate() == null) ? 1 : calculateLeaveCount(request.startDate(), request.endDate());

        if (request.type() == LeaveType.HALF) {
            leaveCount *= 0.5;
        }

        Leave leave = Leave.builder()
                .user(user)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .type(request.type())
                .leaveCount(leaveCount)
                .status(LeaveStatus.PENDING)
                .reason(request.reason())
                .build();

        leaveRepository.save(leave);
    }

    public double calculateLeaveCount(LocalDateTime startDate, LocalDateTime endDate) {
        return (double) ChronoUnit.DAYS.between(startDate, endDate);
    }

    @Transactional
    public void approveLeave(long leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ApiException(LeaveErrorCode.LEAVE_NOT_FOUND));
        User user = leave.getUser();
        UserLeave userLeave = userLeaveRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException(LeaveErrorCode.USER_LEAVE_NOT_FOUND));

        if (leave.isNotPending()) {
            throw new ApiException(LeaveErrorCode.LEAVE_ALREADY_DONE);
        }

        if (leave.isAnnualLeave() && userLeave.getAnnualLeaveCount() < leave.getLeaveCount()) {
            throw new ApiException(LeaveErrorCode.LEAVE_COUNT_REQUIRED);
        }

        userLeave.useLeave(leave.getLeaveCount(), leave.isAnnualLeave());

        leave.approve();
    }

    @Transactional
    public void rejectLeave(long leaveId, RejectLeaveRequest request) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ApiException(LeaveErrorCode.LEAVE_NOT_FOUND));

        if (leave.isNotPending()) {
            throw new ApiException(LeaveErrorCode.LEAVE_ALREADY_DONE);
        }

        leave.reject(request.rejectReason());
    }

    public int countDatesByUserIdWithMonth(long userId, YearMonth yearMonth) {
        return leaveRepository.findAllByUserId(userId)
                .stream()
                .filter(Leave::isApproved)
                .mapToInt((leave) -> leave.countDaysInYearMonth(yearMonth))
                .sum();
    }
}
