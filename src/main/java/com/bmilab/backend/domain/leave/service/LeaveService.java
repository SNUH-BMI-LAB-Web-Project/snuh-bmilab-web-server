package com.bmilab.backend.domain.leave.service;

import com.bmilab.backend.domain.leave.dto.request.ApplyLeaveRequest;
import com.bmilab.backend.domain.leave.dto.request.RejectLeaveRequest;
import com.bmilab.backend.domain.leave.dto.response.LeaveFindAllResponse;
import com.bmilab.backend.domain.leave.dto.response.UserLeaveResponse;
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
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeaveService {
    private final LeaveRepository leaveRepository;
    private final UserLeaveRepository userLeaveRepository;
    private final UserService userService;

    public LeaveFindAllResponse getLeaves(LocalDate startDate, LocalDate endDate) {
        List<Leave> leaves =
                (startDate != null && endDate != null) ?
                        leaveRepository.findAllByBetweenDates(startDate, endDate) :
                        leaveRepository.findAll();

        return LeaveFindAllResponse.of(leaves);
    }

    public LeaveFindAllResponse getLeavesByAdmin(LeaveStatus status, Pageable pageable) {
        Page<Leave> leaves = (status != null) ?
                leaveRepository.findAllByStatus(status, pageable) :
                leaveRepository.findAll(pageable);

        return LeaveFindAllResponse.of(leaves.getContent(), leaves.getTotalPages());
    }

    public LeaveFindAllResponse getLeaves(LocalDate startDate, LocalDate endDate, LeaveStatus status) {
        List<Leave> leaves =
                (startDate != null && endDate != null) ?
                        leaveRepository.findAllByBetweenDatesAndStatus(startDate, endDate, status) :
                        leaveRepository.findAllByStatus(status);

        return LeaveFindAllResponse.of(leaves);
    }

    public UserLeaveResponse getLeavesByUser(Long userId, Pageable pageable) {
        Page<Leave> leaves = leaveRepository.findAllByUserId(userId, pageable);
        UserLeave userLeave = userLeaveRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(LeaveErrorCode.USER_LEAVE_NOT_FOUND));

        return UserLeaveResponse.of(userLeave, leaves.getContent(), leaves.getTotalPages());
    }

    @Transactional
    public void applyLeave(Long userId, ApplyLeaveRequest request) {
        User user = userService.findUserById(userId);
        LeaveType type = request.type();
        double leaveCount = (request.endDate() == null) ? 1 : calculateLeaveCount(request.startDate(), request.endDate());

        validateLeaveTypeAccessPermission(user, type);

        if (type.isHalf()) {
            leaveCount *= 0.5;
        }

        Leave leave = Leave.builder()
                .user(user)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .type(type)
                .leaveCount(leaveCount)
                .status(LeaveStatus.PENDING)
                .reason(request.reason())
                .build();

        leaveRepository.save(leave);
    }

    public double calculateLeaveCount(LocalDate startDate, LocalDate endDate) {
        return (double) ChronoUnit.DAYS.between(startDate, endDate);
    }

    @Transactional
    public void approveLeave(Long processorId, long leaveId) {
        User processor = userService.findUserById(processorId);
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

        leave.approve(processor, LocalDateTime.now());
    }

    @Transactional
    public void rejectLeave(Long processorId, long leaveId, RejectLeaveRequest request) {

        User processor = userService.findUserById(processorId);
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ApiException(LeaveErrorCode.LEAVE_NOT_FOUND));

        if (leave.isNotPending()) {
            throw new ApiException(LeaveErrorCode.LEAVE_ALREADY_DONE);
        }

        leave.reject(request.rejectReason(), processor, LocalDateTime.now());
    }

    public int countDatesByUserIdWithMonth(long userId, YearMonth yearMonth) {
        return leaveRepository.findAllByUserId(userId)
                .stream()
                .filter(Leave::isApproved)
                .mapToInt((leave) -> leave.countDaysInYearMonth(yearMonth))
                .sum();
    }

    private void validateLeaveTypeAccessPermission(User user, LeaveType type) {
        if ((!user.isAdmin()) && type == LeaveType.ALL) {
            throw new ApiException(LeaveErrorCode.ACCESS_DENIED);
        }
    }
}
