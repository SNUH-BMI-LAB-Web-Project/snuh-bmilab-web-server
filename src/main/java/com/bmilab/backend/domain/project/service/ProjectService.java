package com.bmilab.backend.domain.project.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.exception.FileErrorCode;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.dto.condition.ProjectFilterCondition;
import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.dto.query.GetAllTimelinesQueryResult;
import com.bmilab.backend.domain.project.dto.request.ProjectCompleteRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectRequest;
import com.bmilab.backend.domain.project.dto.response.ProjectDetail;
import com.bmilab.backend.domain.project.dto.response.ProjectFileFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.ProjectFileSummary;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse.ProjectSummary;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.ProjectFile;
import com.bmilab.backend.domain.project.entity.ProjectParticipant;
import com.bmilab.backend.domain.project.entity.ProjectParticipantId;
import com.bmilab.backend.domain.project.enums.ProjectFileType;
import com.bmilab.backend.domain.project.enums.ProjectParticipantType;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.project.exception.ProjectErrorCode;
import com.bmilab.backend.domain.project.repository.ProjectFileRepository;
import com.bmilab.backend.domain.project.repository.ProjectParticipantRepository;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.project.repository.TimelineRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final FileInformationRepository fileInformationRepository;
    private final ProjectFileRepository projectFileRepository;
    private final FileService fileService;
    private final TimelineRepository timelineRepository;

    @Transactional
    public void createNewProject(Long userId, ProjectRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();
        ProjectStatus status = (request.isWaiting()) ?
                ProjectStatus.WAITING : calculateProjectStatus(startDate, endDate);

        Project project = Project.builder()
                .title(request.title())
                .content(request.content())
                .author(user)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .status(status)
                .pi(request.pi())
                .practicalProfessor(request.practicalProfessor())
                .irbId(request.irbId())
                .drbId(request.drbId())
                .category(request.category())
                .build();

        projectRepository.save(project);

        //연구 첨부파일 초기화

        createProjectFiles(request.fileIds(), project, ProjectFileType.GENERAL);
        createProjectFiles(request.irbFileIds(), project, ProjectFileType.IRB);
        createProjectFiles(request.drbFileIds(), project, ProjectFileType.DRB);

        //연구 참가자 및 책임자 초기화

        List<User> leaders = userRepository.findAllById(request.leaderIds());

        leaders.forEach(leader -> {
            ProjectParticipantId projectParticipantId = new ProjectParticipantId(project.getId(), leader.getId());

            ProjectParticipant projectLeader = ProjectParticipant.builder()
                    .id(projectParticipantId)
                    .project(project)
                    .user(leader)
                    .type(ProjectParticipantType.LEADER)
                    .build();

            projectParticipantRepository.save(projectLeader);
        });

        List<User> participants = userRepository.findAllById(request.participantIds());

        participants.forEach(participant -> {
            ProjectParticipantId projectParticipantId = new ProjectParticipantId(project.getId(), participant.getId());

            ProjectParticipant projectParticipant = ProjectParticipant.builder()
                    .id(projectParticipantId)
                    .project(project)
                    .user(participant)
                    .type(ProjectParticipantType.PARTICIPANT)
                    .build();

            projectParticipantRepository.save(projectParticipant);
        });
    }

    public void createProjectFiles(List<UUID> fileIds, Project project, ProjectFileType fileType) {
        List<FileInformation> files = fileInformationRepository.findAllById(fileIds);

        files.forEach(file -> file.updateDomain(FileDomainType.PROJECT, project.getId()));

        List<ProjectFile> projectFiles = files
                .stream()
                .map(file -> {
                    return ProjectFile.builder()
                            .fileId(file.getId())
                            .fileInformation(file)
                            .type(fileType)
                            .build();
                }).toList();

        projectFileRepository.saveAll(projectFiles);
    }

    public ProjectFindAllResponse getAllProjects(Pageable pageable, String search, ProjectFilterCondition condition) {
        Page<GetAllProjectsQueryResult> queryResults = projectRepository.findAllBySearch(search, pageable, condition);

        return ProjectFindAllResponse
                .builder()
                .projects(
                        queryResults.getContent()
                                .stream()
                                .map(ProjectSummary::from)
                                .toList()
                )
                .totalPage(queryResults.getTotalPages())
                .build();
    }

    public ProjectDetail getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        List<ProjectParticipant> participants = projectParticipantRepository.findAllByProjectId(projectId);

        List<ProjectFile> projectFiles = projectFileRepository.findAllByProjectId(projectId);

        return ProjectDetail.from(project, participants, projectFiles);
    }

    @Transactional
    public void updateProject(Long editorId, Long projectId, ProjectRequest request) {
        User editor = userRepository.findById(editorId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();

        ProjectStatus status =
                (request.isWaiting()) ? ProjectStatus.WAITING : calculateProjectStatus(startDate, endDate);

        if (!project.canBeEditedBy(editor)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        project.update(
                request.title(),
                request.content(),
                startDate,
                endDate,
                request.category(),
                status
        );

        List<Long> updatedParticipantIds = request.participantIds();
        List<Long> participantIds = projectParticipantRepository.findAllUserIdsByProjectIdAndLeader(projectId, false);

        List<Long> updatedLeaderIds = request.leaderIds();
        List<Long> leaderIds = projectParticipantRepository.findAllUserIdsByProjectIdAndLeader(projectId, true);

        updateParticipants(project, updatedParticipantIds, participantIds, false);
        updateParticipants(project, updatedLeaderIds, leaderIds, true);

        createProjectFiles(request.fileIds(), project, ProjectFileType.GENERAL);
        createProjectFiles(request.irbFileIds(), project, ProjectFileType.IRB);
        createProjectFiles(request.drbFileIds(), project, ProjectFileType.DRB);
    }

    @Transactional
    public void deleteProjectById(Long userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!project.canBeEditedBy(user)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.PROJECT, project.getId());
        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void deleteProjectFile(Long userId, Long projectId, UUID fileId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!project.canBeEditedBy(user)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        FileInformation file = fileInformationRepository.findById(fileId)
                .orElseThrow(() -> new ApiException(FileErrorCode.FILE_NOT_FOUND));

        ProjectFile projectFile = projectFileRepository.findByFileInformation(file)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_FILE_NOT_FOUND));

        s3Service.deleteFile(file.getUploadUrl());
        projectFileRepository.delete(projectFile);
    }

    @Transactional
    public void completeProject(Long userId, Long projectId, ProjectCompleteRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!project.canBeEditedBy(user)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        project.complete(request.endDate());
    }

    private ProjectStatus calculateProjectStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (endDate != null && today.isAfter(endDate)) {
            return ProjectStatus.COMPLETED;
        }

        if (startDate != null) {
            if (today.isBefore(startDate)) {
                return ProjectStatus.PENDING;
            }

            if (today.isEqual(startDate) || today.isAfter(startDate)) {
                return ProjectStatus.IN_PROGRESS;
            }
        }

        return ProjectStatus.WAITING;
    }

    private void updateParticipants(Project project, List<Long> updatedIds, List<Long> participantIds,
                                    boolean updateLeader) {
        Set<Long> intersection = new HashSet<>(participantIds);

        intersection.retainAll(updatedIds);

        Set<Long> newIds = new HashSet<>(updatedIds);

        newIds.removeAll(intersection);

        Set<Long> deletedIds = new HashSet<>(participantIds);

        deletedIds.removeAll(intersection);

        List<User> newUsers = userRepository.findAllByIds(newIds);

        newUsers.forEach(user -> {
            ProjectParticipantId projectParticipantId = new ProjectParticipantId(project.getId(), user.getId());

            ProjectParticipant newParticipant = ProjectParticipant
                    .builder()
                    .id(projectParticipantId)
                    .project(project)
                    .user(user)
                    .type((updateLeader) ? ProjectParticipantType.LEADER : ProjectParticipantType.PARTICIPANT)
                    .build();

            projectParticipantRepository.save(newParticipant);
        });

        deletedIds.forEach(userId ->
                projectParticipantRepository.deleteByProjectIdAndUserId(project.getId(), userId)
        );
    }

    public ProjectFileFindAllResponse getAllProjectFiles(Long projectId) {
        List<ProjectFile> projectFiles = projectFileRepository.findAllByProjectId(projectId);

        List<GetAllTimelinesQueryResult> timelineResults = timelineRepository.findAllResultsByProjectId(
                projectId);

        List<ProjectFileSummary> timelineFileSummaries = timelineResults.stream()
                .flatMap(result -> result.files().stream())
                .map(file -> ProjectFileSummary.from(file, ProjectFileType.MEETING))
                .toList();

        List<ProjectFileSummary> fileSummaries = projectFiles
                .stream()
                .map(ProjectFileSummary::from)
                .collect(Collectors.toList());

        fileSummaries.addAll(timelineFileSummaries);

        return new ProjectFileFindAllResponse(fileSummaries);
    }
}
