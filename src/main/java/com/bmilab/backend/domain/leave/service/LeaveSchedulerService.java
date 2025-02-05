package com.bmilab.backend.domain.leave.service;

import com.bmilab.backend.domain.leave.repository.LeaveRepository;
import com.bmilab.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveSchedulerService {
    private final UserRepository userRepository;

//    @Scheduled
//    @Transactional
//    public void updateAnnualLeave() {
//
//    }
}
