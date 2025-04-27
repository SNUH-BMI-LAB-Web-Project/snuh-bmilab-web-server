package com.bmilab.backend.domain.project.service;

import com.bmilab.backend.domain.project.dto.condition.ProjectFilterCondition;
import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.dto.request.ProjectCompleteRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectFileRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectRequest;
import com.bmilab.backend.domain.project.dto.response.ProjectDetail;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse.ProjectSummary;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.ProjectParticipant;
import com.bmilab.backend.domain.project.entity.ProjectParticipantId;
import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.project.exception.ProjectErrorCode;
import com.bmilab.backend.domain.project.repository.ProjectParticipantRepository;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final ProjectParticipantRepository projectParticipantRepository;

    @Transactional
    public void createNewProject(Long userId, List<MultipartFile> files, ProjectRequest request) {
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
                .category(request.category())
                .build();

        projectRepository.save(project);

        if (files != null && !files.isEmpty()) {

            List<String> fileUrls = files.stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .map(notNullFile -> uploadProjectFile(notNullFile, project))
                    .toList();

            project.updateFileUrls(fileUrls);
        }

        List<User> leaders = userRepository.findAllById(request.leaderIds());

        leaders.forEach(leader -> {
            ProjectParticipantId projectParticipantId = new ProjectParticipantId(project.getId(), leader.getId());

            ProjectParticipant projectLeader = ProjectParticipant.builder()
                    .id(projectParticipantId)
                    .project(project)
                    .user(leader)
                    .isLeader(true)
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
                    .build();

            projectParticipantRepository.save(projectParticipant);
        });
    }

    @Transactional
    public void addProjectFile(Long userId, Long projectId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!project.canBeEditedBy(user)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        List<String> fileUrls = project.getFileUrls();

        fileUrls.add(uploadProjectFile(file, project));

        project.updateFileUrls(fileUrls);
    }

    public ProjectFindAllResponse getAllProjects(int pageNo, int size, String search, ProjectFilterCondition condition) {

        PageRequest pageRequest = PageRequest.of(pageNo, size, Sort.by(Direction.DESC, "createdAt"));
        Page<GetAllProjectsQueryResult> queryResults = projectRepository.findAllBySearch(search, pageRequest, condition);

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

        return ProjectDetail.from(project, participants);
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

        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void deleteProjectFile(Long userId, Long projectId, ProjectFileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!project.canBeEditedBy(user)) {
            throw new ApiException(ProjectErrorCode.PROJECT_ACCESS_DENIED);
        }

        String fileUrl = request.fileUrl();
        List<String> fileUrls = project.getFileUrls();

        s3Service.deleteFile(fileUrl);

        fileUrls.remove(fileUrl);
        project.updateFileUrls(fileUrls);
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

    private String uploadProjectFile(MultipartFile file, Project project) {
        String originalName = file.getOriginalFilename();
        String fileName = originalName.substring(0, originalName.lastIndexOf("."));
        String newFileDir = "projects/" + project.getId() + "/" + fileName;

        return s3Service.uploadFile(file, newFileDir);
    }

    private ProjectStatus calculateProjectStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (endDate != null && today.isAfter(endDate)) {
            return ProjectStatus.COMPLETED;
        }

        if (startDate != null) {
            if (today.isBefore(startDate))
                return ProjectStatus.PENDING;

            if (today.isEqual(startDate) || today.isAfter(startDate))
                return ProjectStatus.IN_PROGRESS;
        }

        return ProjectStatus.WAITING;
    }


    private void updateParticipants(Project project, List<Long> updatedIds, List<Long> participantIds, boolean updateLeader) {
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
                    .isLeader(updateLeader)
                    .build();

            projectParticipantRepository.save(newParticipant);
        });

        deletedIds.forEach(userId ->
                projectParticipantRepository.deleteByProjectIdAndUserId(project.getId(), userId)
        );
    }
}
