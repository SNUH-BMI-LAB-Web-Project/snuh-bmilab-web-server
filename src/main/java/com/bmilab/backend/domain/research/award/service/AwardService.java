package com.bmilab.backend.domain.research.award.service;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.research.award.dto.request.CreateAwardRequest;
import com.bmilab.backend.domain.research.award.dto.request.UpdateAwardRequest;
import com.bmilab.backend.domain.research.award.dto.response.AwardResponse;
import com.bmilab.backend.domain.research.award.dto.response.AwardSummaryResponse;
import com.bmilab.backend.domain.research.award.entity.Award;
import com.bmilab.backend.domain.research.award.entity.AwardRecipient;
import com.bmilab.backend.domain.research.award.repository.AwardRecipientRepository;
import com.bmilab.backend.domain.research.award.repository.AwardRepository;
import com.bmilab.backend.domain.research.award.exception.AwardErrorCode;
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
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardRecipientRepository awardRecipientRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final com.bmilab.backend.domain.user.repository.UserRepository userRepository;

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
        if (dto.awardRecipients() != null && !dto.awardRecipients().isEmpty()) {
            List<Long> userIds = dto.awardRecipients().stream()
                    .map(CreateAwardRequest.AwardRecipientRequest::userId)
                    .collect(Collectors.toList());
            List<User> users = userRepository.findAllById(userIds);
            if (users.size() != userIds.size()) {
                throw new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND);
            }
            java.util.Map<Long, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::getId, user -> user));

            for (CreateAwardRequest.AwardRecipientRequest recipientRequest : dto.awardRecipients()) {
                User user = userMap.get(recipientRequest.userId());
                AwardRecipient recipient = AwardRecipient.builder()
                        .award(newAward)
                        .user(user)
                        .role(recipientRequest.role())
                        .build();
                awardRecipientRepository.save(recipient);
            }
        }

        return new AwardResponse(newAward);
    }

    public void deleteAward(Long userId, boolean isAdmin, Long awardId) {
        if (!isAdmin) {
            throw new ApiException(AwardErrorCode.AWARD_ACCESS_DENIED);
        }
        awardRepository.deleteById(awardId);
    }

    @Transactional(readOnly = true)
    public AwardResponse getAward(Long awardId) {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new ApiException(AwardErrorCode.AWARD_NOT_FOUND));
        return new AwardResponse(award);
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
        if (dto.awardRecipients() != null && !dto.awardRecipients().isEmpty()) {
            List<Long> userIds = dto.awardRecipients().stream()
                    .map(UpdateAwardRequest.AwardRecipientRequest::userId)
                    .collect(Collectors.toList());
            List<User> users = userRepository.findAllById(userIds);
            if (users.size() != userIds.size()) {
                throw new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND);
            }
            java.util.Map<Long, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::getId, user -> user));

            for (UpdateAwardRequest.AwardRecipientRequest recipientRequest : dto.awardRecipients()) {
                User user = userMap.get(recipientRequest.userId());
                AwardRecipient recipient = AwardRecipient.builder()
                        .award(award)
                        .user(user)
                        .role(recipientRequest.role())
                        .build();
                awardRecipientRepository.save(recipient);
            }
        }

        return new AwardResponse(award);
    }

    @Transactional(readOnly = true)
    public Page<AwardSummaryResponse> getAwards(String keyword, Pageable pageable) {
        return awardRepository.findAllBy(keyword, pageable);
    }
}
