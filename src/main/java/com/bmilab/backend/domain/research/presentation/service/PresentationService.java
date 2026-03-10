package com.bmilab.backend.domain.research.presentation.service;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.research.presentation.exception.PresentationErrorCode;
import com.bmilab.backend.domain.research.presentation.dto.request.CreateAcademicPresentationRequest;
import com.bmilab.backend.domain.research.presentation.dto.request.UpdateAcademicPresentationRequest;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationFindAllResponse;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationResponse;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationSummaryResponse;
import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentationAuthor;
import com.bmilab.backend.domain.research.presentation.repository.AcademicPresentationAuthorRepository;
import com.bmilab.backend.domain.research.presentation.repository.AcademicPresentationRepository;
import com.bmilab.backend.domain.research.service.AuthorSyncService;
import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.repository.TaskRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.exception.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PresentationService {

    private final AcademicPresentationRepository academicPresentationRepository;
    private final AcademicPresentationAuthorRepository academicPresentationAuthorRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AuthorSyncService authorSyncService;
    private final UserRepository userRepository;

    public AcademicPresentationResponse createAcademicPresentation(Long userId, CreateAcademicPresentationRequest dto) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
        Task task = null;
        if (dto.taskId() != null) {
            task = taskRepository.findById(dto.taskId())
                    .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
        }
        AcademicPresentation newAcademicPresentation = AcademicPresentation.builder()
                .authors(dto.authors())
                .academicPresentationStartDate(dto.academicPresentationStartDate())
                .academicPresentationEndDate(dto.academicPresentationEndDate())
                .academicPresentationLocation(dto.academicPresentationLocation())
                .academicPresentationHost(dto.academicPresentationHost())
                .academicPresentationName(dto.academicPresentationName())
                .presentationType(dto.presentationType())
                .presentationTitle(dto.presentationTitle())
                .project(project)
                .task(task)
                .createdBy(creator)
                .build();
        academicPresentationRepository.save(newAcademicPresentation);

        // Handle AcademicPresentationAuthor linking
        List<AcademicPresentationAuthor> authors = authorSyncService.syncAuthors(
                dto.academicPresentationAuthors(),
                CreateAcademicPresentationRequest.AcademicPresentationAuthorRequest::userId,
                CreateAcademicPresentationRequest.AcademicPresentationAuthorRequest::role,
                (user, role) -> AcademicPresentationAuthor.builder()
                        .academicPresentation(newAcademicPresentation)
                        .user(user)
                        .role(role)
                        .build()
        );
        authors.forEach(academicPresentationAuthorRepository::save);

        return new AcademicPresentationResponse(newAcademicPresentation, authors);
    }

    public void deleteAcademicPresentation(Long userId, boolean isAdmin, Long academicPresentationId) {
        AcademicPresentation presentation = academicPresentationRepository.findById(academicPresentationId)
                .orElseThrow(() -> new ApiException(PresentationErrorCode.ACADEMIC_PRESENTATION_NOT_FOUND));
        validateAdminOrCreator(userId, isAdmin, presentation.getCreatedBy());
        academicPresentationAuthorRepository.deleteAllByAcademicPresentationId(academicPresentationId);
        academicPresentationRepository.deleteById(academicPresentationId);
    }

    @Transactional(readOnly = true)
    public AcademicPresentationResponse getAcademicPresentation(Long academicPresentationId) {
        AcademicPresentation academicPresentation = academicPresentationRepository.findById(academicPresentationId)
                .orElseThrow(() -> new ApiException(PresentationErrorCode.ACADEMIC_PRESENTATION_NOT_FOUND));
        List<AcademicPresentationAuthor> authors = academicPresentationAuthorRepository.findAllByAcademicPresentationId(academicPresentationId);
        return new AcademicPresentationResponse(academicPresentation, authors);
    }

    public AcademicPresentationResponse updateAcademicPresentation(Long userId, boolean isAdmin, Long academicPresentationId, UpdateAcademicPresentationRequest dto) {
        AcademicPresentation academicPresentation = academicPresentationRepository.findById(academicPresentationId)
                .orElseThrow(() -> new ApiException(PresentationErrorCode.ACADEMIC_PRESENTATION_NOT_FOUND));
        validateAdminOrCreator(userId, isAdmin, academicPresentation.getCreatedBy());
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
        Task task = null;
        if (dto.taskId() != null) {
            task = taskRepository.findById(dto.taskId())
                    .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
        }
        academicPresentation.update(dto.authors(), dto.academicPresentationStartDate(), dto.academicPresentationEndDate(), dto.academicPresentationLocation(), dto.academicPresentationHost(), dto.academicPresentationName(), dto.presentationType(), dto.presentationTitle(), project, task);

        // Handle AcademicPresentationAuthor linking
        academicPresentationAuthorRepository.deleteAllByAcademicPresentationId(academicPresentationId);
        List<AcademicPresentationAuthor> authors = authorSyncService.syncAuthors(
                dto.academicPresentationAuthors(),
                UpdateAcademicPresentationRequest.AcademicPresentationAuthorRequest::userId,
                UpdateAcademicPresentationRequest.AcademicPresentationAuthorRequest::role,
                (user, role) -> AcademicPresentationAuthor.builder()
                        .academicPresentation(academicPresentation)
                        .user(user)
                        .role(role)
                        .build()
        );
        authors.forEach(academicPresentationAuthorRepository::save);

        return new AcademicPresentationResponse(academicPresentation, authors);
    }

    @Transactional(readOnly = true)
    public AcademicPresentationFindAllResponse getAcademicPresentations(String keyword, Pageable pageable) {
        Page<AcademicPresentation> presentationPage = academicPresentationRepository.findAllBy(keyword, pageable);

        List<AcademicPresentationSummaryResponse> presentations = presentationPage.getContent().stream()
                .map(presentation -> {
                    List<AcademicPresentationAuthor> authors =
                            academicPresentationAuthorRepository.findAllByAcademicPresentationId(presentation.getId());
                    return AcademicPresentationSummaryResponse.from(presentation, authors);
                })
                .toList();

        return AcademicPresentationFindAllResponse.of(presentations, presentationPage.getTotalPages());
    }

    private void validateAdminOrCreator(Long userId, boolean isAdmin, User createdBy) {
        if (!isAdmin && (createdBy == null || !createdBy.getId().equals(userId))) {
            throw new ApiException(PresentationErrorCode.ACADEMIC_PRESENTATION_ACCESS_DENIED);
        }
    }
}
