package com.bmilab.backend.domain.research.patent.service;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.research.patent.exception.PatentErrorCode;
import com.bmilab.backend.domain.research.patent.dto.request.CreatePatentRequest;
import com.bmilab.backend.domain.research.patent.dto.request.UpdatePatentRequest;
import com.bmilab.backend.domain.research.patent.dto.response.PatentResponse;
import com.bmilab.backend.domain.research.patent.dto.response.PatentSummaryResponse;
import com.bmilab.backend.domain.research.patent.entity.Patent;
import com.bmilab.backend.domain.research.patent.entity.PatentAuthor;
import com.bmilab.backend.domain.research.patent.repository.PatentAuthorRepository;
import com.bmilab.backend.domain.research.patent.repository.PatentRepository;
import com.bmilab.backend.domain.research.service.AuthorSyncService;
import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.repository.TaskRepository;
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
public class PatentService {

    private final PatentRepository patentRepository;
    private final PatentAuthorRepository patentAuthorRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final FileService fileService;
    private final AuthorSyncService authorSyncService;

    public PatentResponse createPatent(CreatePatentRequest dto) {
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
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

        List<PatentAuthor> patentAuthors = authorSyncService.syncAuthors(
                dto.patentAuthors(),
                CreatePatentRequest.PatentAuthorRequest::userId,
                CreatePatentRequest.PatentAuthorRequest::role,
                (user, role) -> PatentAuthor.builder()
                        .patent(newPatent)
                        .user(user)
                        .role(role)
                        .build()
        );
        patentAuthors.forEach(patentAuthorRepository::save);

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
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
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
        patentAuthorRepository.deleteAllByPatentId(patentId); // Delete existing
        List<PatentAuthor> patentAuthors = authorSyncService.syncAuthors(
                dto.patentAuthors(),
                UpdatePatentRequest.PatentAuthorRequest::userId,
                UpdatePatentRequest.PatentAuthorRequest::role,
                (user, role) -> PatentAuthor.builder()
                        .patent(patent)
                        .user(user)
                        .role(role)
                        .build()
        );
        patentAuthors.forEach(patentAuthorRepository::save);

        return new PatentResponse(patent, patentAuthors, fileSummaries);
    }

    @Transactional(readOnly = true)
    public Page<PatentSummaryResponse> getPatents(String keyword, Pageable pageable) {
        return patentRepository.findAllBy(keyword, pageable);
    }
}
