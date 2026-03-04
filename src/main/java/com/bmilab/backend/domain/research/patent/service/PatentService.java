package com.bmilab.backend.domain.research.patent.service;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.repository.ExternalProfessorRepository;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.research.patent.exception.PatentErrorCode;
import com.bmilab.backend.domain.research.patent.dto.request.CreatePatentRequest;
import com.bmilab.backend.domain.research.patent.dto.request.UpdatePatentRequest;
import com.bmilab.backend.domain.research.patent.dto.response.PatentFindAllResponse;
import com.bmilab.backend.domain.research.patent.dto.response.PatentResponse;
import com.bmilab.backend.domain.research.patent.dto.response.PatentSummaryResponse;
import com.bmilab.backend.domain.research.patent.entity.Patent;
import com.bmilab.backend.domain.research.patent.entity.PatentAuthor;
import com.bmilab.backend.domain.research.patent.repository.PatentAuthorRepository;
import com.bmilab.backend.domain.research.patent.repository.PatentRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PatentService {

    private final PatentRepository patentRepository;
    private final PatentAuthorRepository patentAuthorRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ExternalProfessorRepository externalProfessorRepository;
    private final FileService fileService;

    public PatentResponse createPatent(CreatePatentRequest dto) {
        Project project = dto.projectId() != null
                ? projectRepository.findById(dto.projectId()).orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND))
                : null;
        Task task = dto.taskId() != null
                ? taskRepository.findById(dto.taskId()).orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND))
                : null;
        Patent newPatent = Patent.builder()
                .applicationDate(dto.applicationDate())
                .applicationNumber(dto.applicationNumber())
                .patentName(dto.patentName())
                .applicantsAll(dto.applicantsAll())
                .remarks(dto.remarks())
                .project(project)
                .task(task)
                .build();
        patentRepository.save(newPatent);

        fileService.syncFiles(dto.fileIds(), FileDomainType.PATENT_ATTACHMENT, newPatent.getId());
        List<FileSummary> fileSummaries = fileService.findAllByDomainTypeAndEntityId(FileDomainType.PATENT_ATTACHMENT, newPatent.getId())
                .stream()
                .map(FileSummary::from)
                .toList();

        List<PatentAuthor> patentAuthors = syncPatentAuthors(
                newPatent,
                dto.patentAuthors(),
                CreatePatentRequest.PatentAuthorRequest::userId,
                CreatePatentRequest.PatentAuthorRequest::externalProfessorId,
                CreatePatentRequest.PatentAuthorRequest::role
        );

        return new PatentResponse(newPatent, patentAuthors, fileSummaries);
    }

    public void deletePatent(Long userId, boolean isAdmin, Long patentId) {
        if (!isAdmin) {
            throw new ApiException(PatentErrorCode.PATENT_ACCESS_DENIED);
        }
        patentAuthorRepository.deleteAllByPatentId(patentId); // Explicitly delete PatentAuthors
        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.PATENT_ATTACHMENT, patentId);
        patentRepository.deleteById(patentId);
    }

    @Transactional(readOnly = true)
    public PatentResponse getPatent(Long patentId) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(() -> new ApiException(PatentErrorCode.PATENT_NOT_FOUND));
        List<FileSummary> fileSummaries = fileService.findAllByDomainTypeAndEntityId(FileDomainType.PATENT_ATTACHMENT, patentId)
                .stream()
                .map(FileSummary::from)
                .toList();
        List<PatentAuthor> patentAuthors = patentAuthorRepository.findAllByPatentId(patentId);
        return new PatentResponse(patent, patentAuthors, fileSummaries);
    }

    public PatentResponse updatePatent(Long patentId, UpdatePatentRequest dto) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(() -> new ApiException(PatentErrorCode.PATENT_NOT_FOUND));
        Project project = dto.projectId() != null
                ? projectRepository.findById(dto.projectId()).orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND))
                : null;
        Task task = dto.taskId() != null
                ? taskRepository.findById(dto.taskId()).orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND))
                : null;
        patent.update(dto.applicationDate(), dto.applicationNumber(), dto.patentName(), dto.applicantsAll(), dto.remarks(), project, task);

        fileService.syncFiles(dto.fileIds(), FileDomainType.PATENT_ATTACHMENT, patentId);
        List<FileSummary> fileSummaries = fileService.findAllByDomainTypeAndEntityId(FileDomainType.PATENT_ATTACHMENT, patentId)
                .stream()
                .map(FileSummary::from)
                .toList();

        // Update PatentAuthors
        patentAuthorRepository.deleteAllByPatentId(patentId);
        List<PatentAuthor> patentAuthors = syncPatentAuthors(
                patent,
                dto.patentAuthors(),
                UpdatePatentRequest.PatentAuthorRequest::userId,
                UpdatePatentRequest.PatentAuthorRequest::externalProfessorId,
                UpdatePatentRequest.PatentAuthorRequest::role
        );

        return new PatentResponse(patent, patentAuthors, fileSummaries);
    }

    @Transactional(readOnly = true)
    public PatentFindAllResponse getPatents(String keyword, Pageable pageable) {
        Page<Patent> patentPage = patentRepository.findAllBy(keyword, pageable);

        List<PatentSummaryResponse> patents = patentPage.getContent().stream()
                .map(patent -> {
                    List<PatentAuthor> patentAuthors =
                            patentAuthorRepository.findAllByPatentId(patent.getId());
                    List<FileSummary> files = fileService.findAllByDomainTypeAndEntityId(
                                    FileDomainType.PATENT_ATTACHMENT, patent.getId())
                            .stream()
                            .map(FileSummary::from)
                            .toList();
                    return PatentSummaryResponse.from(patent, patentAuthors, files);
                })
                .toList();

        return PatentFindAllResponse.of(patents, patentPage.getTotalPages());
    }

    private <T> List<PatentAuthor> syncPatentAuthors(
            Patent patent,
            List<T> authorRequests,
            java.util.function.Function<T, Long> userIdExtractor,
            java.util.function.Function<T, Long> externalProfessorIdExtractor,
            java.util.function.Function<T, String> roleExtractor
    ) {
        if (authorRequests == null || authorRequests.isEmpty()) {
            return new ArrayList<>();
        }

        // 내부 사용자 ID 추출
        List<Long> userIds = authorRequests.stream()
                .map(userIdExtractor)
                .filter(id -> id != null)
                .collect(Collectors.toList());

        // 외부 인사 ID 추출
        List<Long> externalProfessorIds = authorRequests.stream()
                .map(externalProfessorIdExtractor)
                .filter(id -> id != null)
                .collect(Collectors.toList());

        // User 조회
        Map<Long, User> userMap = userIds.isEmpty() ? Map.of() :
                userRepository.findAllById(userIds).stream()
                        .collect(Collectors.toMap(User::getId, user -> user));

        // ExternalProfessor 조회
        Map<Long, ExternalProfessor> externalProfessorMap = externalProfessorIds.isEmpty() ? Map.of() :
                externalProfessorRepository.findAllById(externalProfessorIds).stream()
                        .collect(Collectors.toMap(ExternalProfessor::getId, ep -> ep));

        // 검증: 요청한 ID가 모두 존재하는지
        if (userMap.size() != userIds.size() || externalProfessorMap.size() != externalProfessorIds.size()) {
            throw new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND);
        }

        // PatentAuthor 생성
        List<PatentAuthor> patentAuthors = new ArrayList<>();
        for (T request : authorRequests) {
            Long userId = userIdExtractor.apply(request);
            Long externalProfessorId = externalProfessorIdExtractor.apply(request);
            String role = roleExtractor.apply(request);

            // userId와 externalProfessorId 중 하나만 있어야 함
            if ((userId == null && externalProfessorId == null) || (userId != null && externalProfessorId != null)) {
                throw new ApiException(PatentErrorCode.INVALID_AUTHOR_REQUEST);
            }

            PatentAuthor author = PatentAuthor.builder()
                    .patent(patent)
                    .user(userId != null ? userMap.get(userId) : null)
                    .externalProfessor(externalProfessorId != null ? externalProfessorMap.get(externalProfessorId) : null)
                    .role(role)
                    .build();
            patentAuthorRepository.save(author);
            patentAuthors.add(author);
        }

        return patentAuthors;
    }

}
