package com.bmilab.backend.domain.project.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.dto.query.GetAllTimelinesQueryResult;
import com.bmilab.backend.domain.project.dto.request.TimelineRequest;
import com.bmilab.backend.domain.project.dto.response.TimelineFindAllResponse;
import com.bmilab.backend.domain.project.entity.Timeline;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.enums.ProjectAccessPermission;
import com.bmilab.backend.domain.project.exception.ProjectErrorCode;
import com.bmilab.backend.domain.project.exception.TimelineErrorCode;
import com.bmilab.backend.domain.project.repository.TimelineRepository;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
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
    private final ProjectRepository projectRepository;
    private final FileService fileService;
    private final UserService userService;
    private final ProjectService projectService;

    public Timeline findTimelineById(Long timelineId) {
        return timelineRepository.findById(timelineId)
                .orElseThrow(() -> new ApiException(TimelineErrorCode.TIMELINE_NOT_FOUND));
    }

    @Transactional
    public void createTimeline(Long userId, Long projectId, TimelineRequest request) {
        User recorder = userService.findUserById(userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        projectService.validateProjectAccessPermission(project, recorder, ProjectAccessPermission.EDIT, false);

        Timeline timeline = Timeline.builder()
                .project(project)
                .recorder(recorder)
                .title(request.title())
                .date(request.date())
                .startTime(request.startTime())
                .meetingPlace(request.meetingPlace())
                .endTime(request.endTime())
                .type(request.type())
                .summary(request.summary())
                .build();

        timelineRepository.save(timeline);
        fileService.updateAllFileDomainByIds(request.fileIds(), FileDomainType.TIMELINE, timeline.getId());
    }

    public TimelineFindAllResponse getAllTimelinesByProjectId(Long projectId) {
        List<GetAllTimelinesQueryResult> results = timelineRepository.findAllResultsByProjectId(projectId);

        return TimelineFindAllResponse.of(results);
    }


    @Transactional
    public void deleteTimelineFile(Long userId, Long projectId, Long timelineId, UUID fileId) {
        User user = userService.findUserById(userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        projectService.validateProjectAccessPermission(project, user, ProjectAccessPermission.EDIT, false);

        fileService.deleteFile(FileDomainType.TIMELINE, fileId);
    }

    @Transactional
    public void updateTimeline(Long userId, Long projectId, Long timelineId, TimelineRequest request) {
        User user = userService.findUserById(userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        projectService.validateProjectAccessPermission(project, user, ProjectAccessPermission.EDIT, false);

        Timeline timeline = findTimelineById(timelineId);

        timeline.update(
                request.title(),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.meetingPlace(),
                request.type(),
                request.summary()
        );

        fileService.syncFiles(request.fileIds(), FileDomainType.TIMELINE, timeline.getId());
    }

    @Transactional
    public void deleteTimeline(Long userId, Long projectId, Long timelineId) {
        User user = userService.findUserById(userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        projectService.validateProjectAccessPermission(project, user, ProjectAccessPermission.EDIT, false);

        Timeline timeline = findTimelineById(timelineId);

        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.TIMELINE, timeline.getId());

        timelineRepository.delete(timeline);
    }
}
