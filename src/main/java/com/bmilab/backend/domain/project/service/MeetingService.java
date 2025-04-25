package com.bmilab.backend.domain.project.service;

import com.bmilab.backend.domain.project.dto.request.MeetingRequest;
import com.bmilab.backend.domain.project.dto.response.MeetingFindAllResponse;
import com.bmilab.backend.domain.project.entity.Meeting;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.exception.ProjectErrorCode;
import com.bmilab.backend.domain.project.repository.MeetingRepository;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void createMeeting(Long userId, Long projectId, MeetingRequest request) {
        User recorder = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!project.canBeEditedBy(recorder)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        Meeting meeting = Meeting.builder()
                .project(project)
                .recorder(recorder)
                .title(request.title())
                .date(request.date())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .type(request.type())
                .summary(request.summary())
                .content(request.content())
                .build();

        meetingRepository.save(meeting);
    }

    public MeetingFindAllResponse getAllMeetingsByProjectId(Long projectId) {
        List<Meeting> meetings = meetingRepository.findAllByProjectId(projectId);

        return MeetingFindAllResponse.of(meetings);
    }
}
