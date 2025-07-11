package com.bmilab.backend.domain.project.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.exception.FileErrorCode;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.dto.ExternalProfessorSummary;
import com.bmilab.backend.domain.project.dto.condition.ProjectFilterCondition;
import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.dto.query.GetAllTimelinesQueryResult;
import com.bmilab.backend.domain.project.dto.request.ExternalProfessorRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectCompleteRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectRequest;
import com.bmilab.backend.domain.project.dto.response.ExternalProfessorFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.ProjectDetail;
import com.bmilab.backend.domain.project.dto.response.ProjectFileFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.ProjectFileSummary;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse.ProjectSummary;
import com.bmilab.backend.domain.project.dto.response.SearchProjectResponse;
import com.bmilab.backend.domain.project.dto.response.UserProjectFindAllResponse;
import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.ProjectFile;
import com.bmilab.backend.domain.project.entity.ProjectParticipant;
import com.bmilab.backend.domain.project.entity.ProjectParticipantId;
import com.bmilab.backend.domain.project.enums.ProjectAccessPermission;
import com.bmilab.backend.domain.project.enums.ProjectFileType;
import com.bmilab.backend.domain.project.enums.ProjectParticipantType;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.project.event.ProjectUpdateEvent;
import com.bmilab.backend.domain.project.exception.ProjectErrorCode;
import com.bmilab.backend.domain.project.repository.ExternalProfessorRepository;
import com.bmilab.backend.domain.project.repository.ProjectFileRepository;
import com.bmilab.backend.domain.project.repository.ProjectParticipantRepository;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.project.repository.TimelineRepository;
import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.projectcategory.service.ProjectCategoryService;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final S3Service s3Service;
    private final ApplicationEventPublisher eventPublisher;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final FileInformationRepository fileInformationRepository;
    private final ProjectFileRepository projectFileRepository;
    private final FileService fileService;
    private final TimelineRepository timelineRepository;
    private final ReportRepository reportRepository;
    private final UserService userService;
    private final ProjectCategoryService projectCategoryService;
    private final ExternalProfessorRepository externalProfessorRepository;

    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

    @Transactional
    public void createNewProject(Long userId, ProjectRequest request) {
        User user = userService.findUserById(userId);

        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();
        ProjectStatus status =
                (request.isWaiting()) ? ProjectStatus.WAITING : calculateProjectStatus(startDate, endDate);
        ProjectCategory category = projectCategoryService.findProjectCategoryById(request.categoryId());
        List<String> piList = request.piList()
                .stream()
                .map(this::convertToExProfessorString)
                .toList();
        List<String> practicalProfessors = request.practicalProfessors()
                .stream()
                .map(this::convertToExProfessorString)
                .toList();

        Project project = Project.builder()
                .title(request.title())
                .content(request.content())
                .author(user)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .status(status)
                .pi(piList.isEmpty() ? null : String.join(",", piList))
                .practicalProfessor(practicalProfessors.isEmpty() ? null : String.join(",", practicalProfessors))
                .irbId(request.irbId())
                .drbId(request.drbId())
                .category(category)
                .isPrivate(request.isPrivate())
                .build();

        projectRepository.save(project);

        //연구 첨부파일 초기화

        createProjectFiles(request.fileIds(), project, ProjectFileType.GENERAL);
        createProjectFiles(request.irbFileIds(), project, ProjectFileType.IRB);
        createProjectFiles(request.drbFileIds(), project, ProjectFileType.DRB);

        //연구 참가자 및 책임자 초기화

        List<User> leaders = userService.findAllUsersById(request.leaderIds());

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

        List<User> participants = userService.findAllUsersById(request.participantIds());

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

        eventPublisher.publishEvent(new ProjectUpdateEvent(project));
    }

    public void createProjectFiles(List<UUID> fileIds, Project project, ProjectFileType fileType) {
        if (fileIds.isEmpty()) {
            return;
        }

        List<FileInformation> files = fileInformationRepository.findAllById(fileIds);

        List<ProjectFile> projectFiles = files
                .stream()
                .map(file -> ProjectFile.builder()
                        .fileInformation(file)
                        .type(fileType)
                        .build())
                .toList();

        projectFileRepository.saveAll(projectFiles);

        files.forEach(file -> file.updateDomain(FileDomainType.PROJECT, project.getId()));
    }

    public ProjectFindAllResponse getAllProjects(
            Long userId,
            String search, ProjectFilterCondition condition,
            Pageable pageable
    ) {

        Page<GetAllProjectsQueryResult> queryResults = projectRepository.findAllByFiltering(
                userId,
                search,
                condition,
                pageable
        );

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

    public ProjectDetail getProjectDetailById(Long userId, Long projectId) {
        Project project = findProjectById(projectId);
        User user = userService.findUserById(userId);

        List<ProjectParticipant> participants = projectParticipantRepository.findAllByProjectId(projectId);

        List<ProjectFile> projectFiles = projectFileRepository.findAllByProjectId(projectId);

        validateProjectAccessPermission(project, user, ProjectAccessPermission.EDIT, true);

        return ProjectDetail.from(project, participants, projectFiles, true);
    }

    @Transactional
    public void updateProject(Long editorId, Long projectId, ProjectRequest request) {
        User editor = userService.findUserById(editorId);
        Project project = findProjectById(projectId);
        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();
        ProjectCategory category = projectCategoryService.findProjectCategoryById(request.categoryId());

        ProjectStatus status =
                (request.isWaiting()) ? ProjectStatus.WAITING : calculateProjectStatus(startDate, endDate);

        validateProjectAccessPermission(project, editor, ProjectAccessPermission.EDIT, false);

        List<String> piList = request.piList()
                .stream()
                .map(this::convertToExProfessorString)
                .toList();
        List<String> practicalProfessors = request.practicalProfessors()
                .stream()
                .map(this::convertToExProfessorString)
                .toList();

        project.update(
                request.title(),
                request.content(),
                startDate,
                endDate,
                piList.isEmpty() ? null : piList,
                practicalProfessors.isEmpty() ? null : practicalProfessors,
                category,
                status,
                request.isPrivate()
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

        eventPublisher.publishEvent(new ProjectUpdateEvent(project));
    }

    @Transactional
    public void deleteProjectById(Long userId, Long projectId) {
        User user = userService.findUserById(userId);
        Project project = findProjectById(projectId);

        validateProjectAccessPermission(project, user, ProjectAccessPermission.DELETE, false);

        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.PROJECT, project.getId());
        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void deleteProjectFile(Long userId, Long projectId, UUID fileId) {
        User user = userService.findUserById(userId);
        Project project = findProjectById(projectId);

        validateProjectAccessPermission(project, user, ProjectAccessPermission.EDIT, false);

        FileInformation file = fileInformationRepository.findById(fileId)
                .orElseThrow(() -> new ApiException(FileErrorCode.FILE_NOT_FOUND));

        ProjectFile projectFile = projectFileRepository.findByFileInformation(file)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_FILE_NOT_FOUND));

        s3Service.deleteFile(file.getUploadUrl());
        projectFileRepository.delete(projectFile);
    }

    @Transactional
    public void completeProject(Long userId, Long projectId, ProjectCompleteRequest request) {
        User user = userService.findUserById(userId);
        Project project = findProjectById(projectId);

        validateProjectAccessPermission(project, user, ProjectAccessPermission.EDIT, false);

        project.complete(request.endDate());
    }

    private ProjectStatus calculateProjectStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        log.info("today: {}, startDate: {}, endDate: {}", today, startDate, endDate);

        if (endDate != null && today.isAfter(endDate)) {
            log.info("today is after endDate, return ProjectStatus.COMPLETED.");
            return ProjectStatus.COMPLETED;
        }

        if (startDate != null) {
            if (today.isBefore(startDate)) {
                log.info("today is before startDate, return ProjectStatus.PENDING.");
                return ProjectStatus.PENDING;
            }

            if (today.isEqual(startDate) || today.isAfter(startDate)) {
                log.info("today is equal to or after startDate, return ProjectStatus.IN_PROGRESS.");
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

        List<User> newUsers = userService.findAllUsersById(newIds);

        List<ProjectParticipant> newParticipants = newUsers.stream()
                .map(user -> {
                    ProjectParticipantId projectParticipantId = new ProjectParticipantId(project.getId(), user.getId());

                    return ProjectParticipant
                            .builder()
                            .id(projectParticipantId)
                            .project(project)
                            .user(user)
                            .type((updateLeader) ? ProjectParticipantType.LEADER : ProjectParticipantType.PARTICIPANT)
                            .build();

                })
                .toList();

        projectParticipantRepository.saveAll(newParticipants);

        deletedIds.forEach(userId ->
                projectParticipantRepository.deleteByProjectIdAndUserId(project.getId(), userId)
        );
    }

    public ProjectFileFindAllResponse getAllProjectFiles(Long userId, Long projectId) {
        User user = userService.findUserById(userId);
        Project project = findProjectById(projectId);

        validateProjectAccessPermission(project, user, ProjectAccessPermission.EDIT, true);

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

    public ReportFindAllResponse getReportsByProject(Long userId, Long projectId, Long filterUserId,
                                                     LocalDate startDate, LocalDate endDate, String keyword) {
        User user = userService.findUserById(userId);
        Project project = findProjectById(projectId);

        validateProjectAccessPermission(project, user, ProjectAccessPermission.EDIT, true);

        List<GetAllReportsQueryResult> results = reportRepository.findReportsByUser(filterUserId, projectId,
                startDate, endDate);

        return ReportFindAllResponse.of(results);
    }

    private ProjectAccessPermission getAccessPermission(Project project, User user) {
        if (project.canBeEditedBy(user)) {
            return ProjectAccessPermission.DELETE;
        }

        Optional<ProjectParticipant> participant = projectParticipantRepository.findByProjectAndUser(project, user);

        if (participant.isEmpty()) {
            return ProjectAccessPermission.NONE;
        }

        ProjectParticipant projectParticipant = participant.get();

        if (projectParticipant.getType() == ProjectParticipantType.PARTICIPANT) {
            return ProjectAccessPermission.EDIT;
        }

        if (projectParticipant.isLeader()) {
            return ProjectAccessPermission.DELETE;
        }

        return ProjectAccessPermission.NONE;
    }

    public SearchProjectResponse searchProject(Long userId, boolean all, String keyword) {
        return SearchProjectResponse.of(projectRepository.searchProject(userId, all, keyword));
    }

    private String convertToExProfessorString(ExternalProfessorSummary exProfessor) {
        return exProfessor.name() + "/" + exProfessor.organization() + "/" + exProfessor.department();
    }

    public void validateProjectAccessPermission(Project project, User user, ProjectAccessPermission permission,
                                                boolean needPrivate) {
        boolean shouldValidate = !needPrivate || project.isPrivate();

        if (shouldValidate && getAccessPermission(project, user).isNotGranted(permission)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }
    }

    public UserProjectFindAllResponse getUserProjects(Long userId) {
        User user = userService.findUserById(userId);
        List<Project> projects = projectRepository.findAllByUser(user);

        return UserProjectFindAllResponse.of(projects);
    }

    @Transactional
    public void createExternalProfessor(ExternalProfessorRequest request) {
        ExternalProfessor externalProfessor = ExternalProfessor.builder()
                .name(request.name())
                .organization(request.organization())
                .department(request.department())
                .position(request.position())
                .build();

        externalProfessorRepository.save(externalProfessor);
    }

    @Transactional
    public void updateExternalProfessor(Long professorId, ExternalProfessorRequest request) {
        ExternalProfessor externalProfessor = externalProfessorRepository.findById(professorId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.EXTERNAL_PROFESSOR_NOT_FOUND));

        externalProfessor.update(request.name(), request.organization(), request.department(), request.position());
    }

    public ExternalProfessorFindAllResponse getAllExternalProfessors() {
        List<ExternalProfessor> externalProfessors = externalProfessorRepository.findAll();

        return ExternalProfessorFindAllResponse.of(externalProfessors);
    }

    @Transactional
    public void deleteExternalProfessor(Long professorId) {
        ExternalProfessor externalProfessor = externalProfessorRepository.findById(professorId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.EXTERNAL_PROFESSOR_NOT_FOUND));

        externalProfessorRepository.delete(externalProfessor);
    }

    public ExternalProfessorFindAllResponse getExternalProfessorsByName(String name) {
        List<ExternalProfessor> externalProfessors = externalProfessorRepository.findAllByNameContaining(name);

        return ExternalProfessorFindAllResponse.of(externalProfessors);
    }

    public StreamingResponseBody downloadIrbFilesByZip(Long projectId) {
        List<ProjectFile> irbFiles = projectFileRepository.findAllIrbFilesByProjectId(projectId);

        if (irbFiles.isEmpty()) {
            throw new ApiException(ProjectErrorCode.IRB_FILES_IS_EMPTY);
        }

        List<String> fileKeys = irbFiles.stream()
                .map(irbFile -> fileService.getFileKey(irbFile.getFileInformation()))
                .toList();

        return s3Service.downloadS3FilesByZip(fileKeys);
    }

    public StreamingResponseBody downloadDrbFilesByZip(Long projectId) {
        List<ProjectFile> drbFiles = projectFileRepository.findAllDrbFilesByProjectId(projectId);

        if (drbFiles.isEmpty()) {
            throw new ApiException(ProjectErrorCode.DRB_FILES_IS_EMPTY);
        }

        List<String> fileKeys = drbFiles.stream()
                .map(drbFile -> fileService.getFileKey(drbFile.getFileInformation()))
                .toList();

        return s3Service.downloadS3FilesByZip(fileKeys);
    }
}
