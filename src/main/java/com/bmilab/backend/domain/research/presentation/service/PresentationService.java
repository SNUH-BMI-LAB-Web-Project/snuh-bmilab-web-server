package com.bmilab.backend.domain.research.presentation.service;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.research.presentation.exception.PresentationErrorCode;
import com.bmilab.backend.domain.research.presentation.dto.request.CreateAcademicPresentationRequest;
import com.bmilab.backend.domain.research.presentation.dto.request.UpdateAcademicPresentationRequest;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationResponse;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationSummaryResponse;
import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentationAuthor;
import com.bmilab.backend.domain.research.presentation.repository.AcademicPresentationAuthorRepository;
import com.bmilab.backend.domain.research.presentation.repository.AcademicPresentationRepository;
import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.repository.TaskRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.exception.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PresentationService {

    private final AcademicPresentationRepository academicPresentationRepository;
    private final AcademicPresentationAuthorRepository academicPresentationAuthorRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final com.bmilab.backend.domain.user.repository.UserRepository userRepository;

    public AcademicPresentationResponse createAcademicPresentation(CreateAcademicPresentationRequest dto) {
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
                .build();
        academicPresentationRepository.save(newAcademicPresentation);

        // Handle AcademicPresentationAuthor linking
        if (dto.academicPresentationAuthors() != null && !dto.academicPresentationAuthors().isEmpty()) {
            List<Long> userIds = dto.academicPresentationAuthors().stream()
                    .map(CreateAcademicPresentationRequest.AcademicPresentationAuthorRequest::userId)
                    .collect(Collectors.toList());
            List<User> users = userRepository.findAllById(userIds);
            if (users.size() != userIds.size()) {
                throw new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND);
            }
            java.util.Map<Long, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::getId, user -> user));

            for (CreateAcademicPresentationRequest.AcademicPresentationAuthorRequest authorRequest : dto.academicPresentationAuthors()) {
                User user = userMap.get(authorRequest.userId());
                AcademicPresentationAuthor author = AcademicPresentationAuthor.builder()
                        .academicPresentation(newAcademicPresentation)
                        .user(user)
                        .role(authorRequest.role())
                        .build();
                academicPresentationAuthorRepository.save(author);
            }
        }

        return new AcademicPresentationResponse(newAcademicPresentation);
    }

    public void deleteAcademicPresentation(Long userId, boolean isAdmin, Long academicPresentationId) {
        if (!isAdmin) {
            throw new ApiException(PresentationErrorCode.ACADEMIC_PRESENTATION_ACCESS_DENIED);
        }
        academicPresentationRepository.deleteById(academicPresentationId);
    }

    @Transactional(readOnly = true)
    public AcademicPresentationResponse getAcademicPresentation(Long academicPresentationId) {
        AcademicPresentation academicPresentation = academicPresentationRepository.findById(academicPresentationId)
                .orElseThrow(() -> new ApiException(PresentationErrorCode.ACADEMIC_PRESENTATION_NOT_FOUND));
        return new AcademicPresentationResponse(academicPresentation);
    }

    public AcademicPresentationResponse updateAcademicPresentation(Long academicPresentationId, UpdateAcademicPresentationRequest dto) {
        AcademicPresentation academicPresentation = academicPresentationRepository.findById(academicPresentationId)
                .orElseThrow(() -> new ApiException(PresentationErrorCode.ACADEMIC_PRESENTATION_NOT_FOUND));
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
        if (dto.academicPresentationAuthors() != null && !dto.academicPresentationAuthors().isEmpty()) {
            List<Long> userIds = dto.academicPresentationAuthors().stream()
                    .map(UpdateAcademicPresentationRequest.AcademicPresentationAuthorRequest::userId)
                    .collect(Collectors.toList());
            List<User> users = userRepository.findAllById(userIds);
            if (users.size() != userIds.size()) {
                throw new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND);
            }
            java.util.Map<Long, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::getId, user -> user));

            for (UpdateAcademicPresentationRequest.AcademicPresentationAuthorRequest authorRequest : dto.academicPresentationAuthors()) {
                User user = userMap.get(authorRequest.userId());
                AcademicPresentationAuthor author = AcademicPresentationAuthor.builder()
                        .academicPresentation(academicPresentation)
                        .user(user)
                        .role(authorRequest.role())
                        .build();
                academicPresentationAuthorRepository.save(author);
            }
        }

        return new AcademicPresentationResponse(academicPresentation);
    }

    @Transactional(readOnly = true)
    public Page<AcademicPresentationSummaryResponse> getAcademicPresentations(String keyword, Pageable pageable) {
        return academicPresentationRepository.findAllBy(keyword, pageable);
    }
}
