package com.bmilab.backend.domain.project.service;

import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.dto.query.GetAllTimelinesQueryResult;
import com.bmilab.backend.domain.project.dto.request.TimelineRequest;
import com.bmilab.backend.domain.project.dto.response.TimelineFindAllResponse;
import com.bmilab.backend.domain.project.entity.Timeline;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.exception.ProjectErrorCode;
import com.bmilab.backend.domain.project.exception.TimelineErrorCode;
import com.bmilab.backend.domain.project.repository.TimelineRepository;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineService {
    private final TimelineRepository timelineRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final FileService fileService;

    @Transactional
    public void createTimeline(Long userId, Long projectId, TimelineRequest request) {
        User recorder = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!project.canBeEditedBy(recorder)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        Timeline timeline = Timeline.builder()
                .project(project)
                .recorder(recorder)
                .title(request.title())
                .date(request.date())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .type(request.type())
                .summary(request.summary())
                .build();

        timelineRepository.save(timeline);
    }

    public TimelineFindAllResponse getAllTimelinesByProjectId(Long projectId) {
        List<GetAllTimelinesQueryResult> results = timelineRepository.findAllResultsByProjectId(projectId);

        return TimelineFindAllResponse.of(results);
    }


    @Transactional
    public void deleteTimelineFile(Long userId, Long projectId, Long timelineId, UUID fileId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!project.canBeEditedBy(user)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        fileService.deleteFile(FileDomainType.TIMELINE, fileId);
    }

    @Transactional
    public void updateTimeline(Long userId, Long projectId, Long timelineId, TimelineRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (project.canBeEditedBy(user)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        Timeline timeline = timelineRepository.findById(timelineId)
                .orElseThrow(() -> new ApiException(TimelineErrorCode.TIMELINE_NOT_FOUND));

        timeline.update(
                request.title(),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.type(),
                request.summary()
        );
    }

    @Transactional
    public void deleteTimeline(Long userId, Long projectId, Long timelineId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (project.canBeEditedBy(user)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        Timeline timeline = timelineRepository.findById(timelineId)
                .orElseThrow(() -> new ApiException(TimelineErrorCode.TIMELINE_NOT_FOUND));

        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.TIMELINE, timeline.getId());

        timelineRepository.delete(timeline);
    }
}
