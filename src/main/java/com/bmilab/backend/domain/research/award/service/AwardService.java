package com.bmilab.backend.domain.research.award.service;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.research.award.dto.request.CreateAwardRequest;
import com.bmilab.backend.domain.research.award.dto.request.UpdateAwardRequest;
import com.bmilab.backend.domain.research.award.dto.response.AwardFindAllResponse;
import com.bmilab.backend.domain.research.award.dto.response.AwardResponse;
import com.bmilab.backend.domain.research.award.dto.response.AwardSummaryResponse;
import com.bmilab.backend.domain.research.award.entity.Award;
import com.bmilab.backend.domain.research.award.entity.AwardRecipient;
import com.bmilab.backend.domain.research.award.repository.AwardRecipientRepository;
import com.bmilab.backend.domain.research.award.repository.AwardRepository;
import com.bmilab.backend.domain.research.award.exception.AwardErrorCode;
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
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardRecipientRepository awardRecipientRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AuthorSyncService authorSyncService;

    public AwardResponse createAward(CreateAwardRequest dto) {
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
        Task task = dto.taskId() != null
                ? taskRepository.findById(dto.taskId()).orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND))
                : null;
        Award newAward = Award.builder()
                .recipients(dto.recipients())
                .awardDate(dto.awardDate())
                .hostInstitution(dto.hostInstitution())
                .competitionName(dto.competitionName())
                .awardName(dto.awardName())
                .presentationTitle(dto.presentationTitle())
                .project(project)
                .task(task)
                .build();
        awardRepository.save(newAward);

        // Handle AwardRecipient linking
        List<AwardRecipient> recipients = authorSyncService.syncAuthors(
                dto.awardRecipients(),
                CreateAwardRequest.AwardRecipientRequest::userId,
                CreateAwardRequest.AwardRecipientRequest::role,
                (user, role) -> AwardRecipient.builder()
                        .award(newAward)
                        .user(user)
                        .role(role)
                        .build()
        );
        recipients.forEach(awardRecipientRepository::save);

        return new AwardResponse(newAward, recipients);
    }

    public void deleteAward(Long userId, boolean isAdmin, Long awardId) {
        if (!isAdmin) {
            throw new ApiException(AwardErrorCode.AWARD_ACCESS_DENIED);
        }
        awardRecipientRepository.deleteAllByAwardId(awardId);
        awardRepository.deleteById(awardId);
    }

    @Transactional(readOnly = true)
    public AwardResponse getAward(Long awardId) {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new ApiException(AwardErrorCode.AWARD_NOT_FOUND));
        List<AwardRecipient> recipients = awardRecipientRepository.findAllByAwardId(awardId);
        return new AwardResponse(award, recipients);
    }

    public AwardResponse updateAward(Long awardId, UpdateAwardRequest dto) {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new ApiException(AwardErrorCode.AWARD_NOT_FOUND));
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
        Task task = dto.taskId() != null
                ? taskRepository.findById(dto.taskId()).orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND))
                : null;
        award.update(dto.recipients(), dto.awardDate(), dto.hostInstitution(), dto.competitionName(), dto.awardName(), dto.presentationTitle(), project, task);

        // Handle AwardRecipient linking
        awardRecipientRepository.deleteAllByAwardId(awardId);
        List<AwardRecipient> recipients = authorSyncService.syncAuthors(
                dto.awardRecipients(),
                UpdateAwardRequest.AwardRecipientRequest::userId,
                UpdateAwardRequest.AwardRecipientRequest::role,
                (user, role) -> AwardRecipient.builder()
                        .award(award)
                        .user(user)
                        .role(role)
                        .build()
        );
        recipients.forEach(awardRecipientRepository::save);

        return new AwardResponse(award, recipients);
    }

    @Transactional(readOnly = true)
    public AwardFindAllResponse getAwards(String keyword, Pageable pageable) {
        Page<Award> awardPage = awardRepository.findAllBy(keyword, pageable);

        List<AwardSummaryResponse> awards = awardPage.getContent().stream()
                .map(award -> {
                    List<AwardRecipient> recipients =
                            awardRecipientRepository.findAllByAwardId(award.getId());
                    return AwardSummaryResponse.from(award, recipients);
                })
                .toList();

        return AwardFindAllResponse.of(awards, awardPage.getTotalPages());
    }
}
