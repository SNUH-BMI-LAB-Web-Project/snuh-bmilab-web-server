package com.bmilab.backend.domain.task.service;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.task.dto.request.TaskAgreementUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskBasicInfoUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPeriodRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPeriodUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPresentationUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskProposalUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskRequest;
import com.bmilab.backend.domain.task.dto.response.TaskAgreementResponse;
import com.bmilab.backend.domain.task.dto.response.TaskBasicInfoResponse;
import com.bmilab.backend.domain.task.dto.response.TaskBasicResponse;
import com.bmilab.backend.domain.task.dto.response.TaskMemberSummary;
import com.bmilab.backend.domain.task.dto.response.TaskPeriodResponse;
import com.bmilab.backend.domain.task.dto.response.TaskPresentationResponse;
import com.bmilab.backend.domain.task.dto.response.TaskProposalResponse;
import com.bmilab.backend.domain.task.dto.response.TaskStatsResponse;
import com.bmilab.backend.domain.task.dto.response.TaskSummaryResponse;
import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.entity.TaskAgreement;
import com.bmilab.backend.domain.task.entity.TaskBasicInfo;
import com.bmilab.backend.domain.task.entity.TaskPeriod;
import com.bmilab.backend.domain.task.entity.TaskPresentation;
import com.bmilab.backend.domain.task.entity.TaskProposal;
import com.bmilab.backend.domain.task.entity.TaskPresentationMaker;
import com.bmilab.backend.domain.task.entity.TaskProposalWriter;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.domain.task.exception.TaskErrorCode;
import com.bmilab.backend.domain.task.repository.*;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskPeriodRepository taskPeriodRepository;
    private final TaskBasicInfoRepository taskBasicInfoRepository;
    private final TaskProposalRepository taskProposalRepository;
    private final TaskProposalWriterRepository taskProposalWriterRepository;
    private final TaskPresentationRepository taskPresentationRepository;
    private final TaskPresentationMakerRepository taskPresentationMakerRepository;
    private final TaskAgreementRepository taskAgreementRepository;
    private final UserService userService;
    private final FileService fileService;


    @Transactional
    public void createTask(Long userId, TaskRequest request) {

        if (request.researchTaskNumber() != null &&
                taskRepository.existsByResearchTaskNumber(request.researchTaskNumber())) {
            throw new ApiException(TaskErrorCode.TASK_DUPLICATE_RESEARCH_NUMBER);
        }

        User practicalManager =
                request.practicalManagerId() != null ? userService.findUserById(request.practicalManagerId()) : null;

        Task task = Task.builder()
                .researchTaskNumber(request.researchTaskNumber())
                .title(request.title())
                .rfpNumber(request.rfpNumber())
                .rfpName(request.rfpName())
                .businessName(request.businessName())
                .issuingAgency(request.issuingAgency())
                .supportType(request.supportType())
                .leadInstitution(request.leadInstitution())
                .leadProfessor(request.leadProfessor())
                .snuhPi(request.snuhPi())
                .professorRole(request.professorRole())
                .practicalManager(practicalManager)
                .participatingInstitutions(request.participatingInstitutions())
                .threeFiveRule(request.threeFiveRule())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .totalYears(request.totalYears())
                .currentYear(request.currentYear())
                .status(request.status() != null ? request.status() : TaskStatus.PROPOSAL_WRITING)
                .build();

        taskRepository.save(task);
    }

    @Transactional
    public void updateTask(Long userId, Long taskId, TaskRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        User practicalManager =
                request.practicalManagerId() != null ? userService.findUserById(request.practicalManagerId()) : null;

        task.update(
                request.researchTaskNumber(),
                request.title(),
                request.rfpNumber(),
                request.rfpName(),
                request.status(),
                request.businessName(),
                request.issuingAgency(),
                request.supportType(),
                request.threeFiveRule(),
                request.startDate(),
                request.endDate(),
                request.totalYears(),
                request.currentYear(),
                request.leadInstitution(),
                request.leadProfessor(),
                request.snuhPi(),
                request.professorRole(),
                practicalManager,
                request.participatingInstitutions()
        );
    }

    public TaskStatsResponse getTaskStats(Long userId) {

        Long totalCount = taskRepository.countAllTasks();
        Long inProgressCount = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        Long proposalWritingCount = taskRepository.countByStatus(TaskStatus.PROPOSAL_WRITING);
        Long presentationPreparingCount = taskRepository.countByStatus(TaskStatus.PRESENTATION_PREPARING);

        return new TaskStatsResponse(totalCount, inProgressCount, proposalWritingCount, presentationPreparingCount);
    }

    public Page<TaskSummaryResponse> getAllTasks(Long userId, TaskStatus status, String keyword, Pageable pageable) {

        Page<Task> tasks = taskRepository.findTasksForList(status, keyword, pageable);
        return tasks.map(TaskSummaryResponse::from);
    }

    public TaskBasicInfoResponse getTaskBasicInfo(Long userId, Long taskId) {

        Task task = getTaskById(taskId);
        TaskBasicInfo basicInfo = taskBasicInfoRepository.findByTask(task).orElse(null);

        List<FileSummary> rfpFiles = fileService.findAllByDomainTypeAndEntityId(FileDomainType.TASK_RFP, taskId)
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> announcementFiles = fileService.findAllByDomainTypeAndEntityId(
                FileDomainType.TASK_ANNOUNCEMENT,
                taskId
        ).stream().map(FileSummary::from).collect(Collectors.toList());

        return TaskBasicInfoResponse.from(
                basicInfo,
                task.getStartDate(),
                task.getEndDate(),
                task.getThreeFiveRule(),
                rfpFiles,
                announcementFiles
        );
    }

    @Transactional
    public void updateBasicInfo(Long userId, Long taskId, TaskBasicInfoUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        TaskBasicInfo basicInfo = taskBasicInfoRepository.findByTask(task)
                .orElseGet(() -> TaskBasicInfo.builder().task(task).build());

        basicInfo.update(
                request.ministry(),
                request.specializedAgency(),
                request.announcementNumber(),
                request.businessContactName(),
                request.businessContactDepartment(),
                request.businessContactEmail(),
                request.businessContactPhone(),
                request.announcementLink()
        );

        if (request.threeFiveRule() != null || request.announcementStartDate() != null ||
                request.announcementEndDate() != null) {
            task.update(
                    task.getResearchTaskNumber(),
                    task.getTitle(),
                    task.getRfpNumber(),
                    task.getRfpName(),
                    task.getStatus(),
                    task.getBusinessName(),
                    task.getIssuingAgency(),
                    task.getSupportType(),
                    request.threeFiveRule() != null ? request.threeFiveRule() : task.getThreeFiveRule(),
                    request.announcementStartDate() != null ? request.announcementStartDate() : task.getStartDate(),
                    request.announcementEndDate() != null ? request.announcementEndDate() : task.getEndDate(),
                    task.getTotalYears(),
                    task.getCurrentYear(),
                    task.getLeadInstitution(),
                    task.getLeadProfessor(),
                    task.getSnuhPi(),
                    task.getProfessorRole(),
                    task.getPracticalManager(),
                    task.getParticipatingInstitutions()
            );
        }

        taskBasicInfoRepository.save(basicInfo);
    }

    @Transactional
    public void updateProposal(Long userId, Long taskId, TaskProposalUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        TaskProposal proposal = taskProposalRepository.findByTask(task)
                .orElseGet(() -> TaskProposal.builder().task(task).build());

        proposal.update(
                request.proposalDeadline(),
                request.contractorContactName(),
                request.contractorContactDepartment(),
                request.contractorContactEmail(),
                request.contractorContactPhone(),
                request.internalContactName(),
                request.internalContactDepartment(),
                request.internalContactEmail(),
                request.internalContactPhone()
        );

        taskProposalRepository.save(proposal);

        taskProposalWriterRepository.deleteByTaskProposal(proposal);

        if (request.proposalWriterIds() != null) {
            for (Long writerId : request.proposalWriterIds()) {
                User writer = userService.findUserById(writerId);
                TaskProposalWriter proposalWriter = TaskProposalWriter.builder()
                        .taskProposal(proposal)
                        .user(writer)
                        .build();
                taskProposalWriterRepository.save(proposalWriter);
            }
        }
    }

    public TaskProposalResponse getTaskProposal(Long userId, Long taskId) {

        Task task = getTaskById(taskId);
        TaskProposal proposal = taskProposalRepository.findByTask(task).orElse(null);

        List<TaskMemberSummary> writers =
                proposal != null ? taskProposalWriterRepository.findByTaskProposal(proposal)
                        .stream()
                        .map(TaskMemberSummary::fromProposalWriter)
                        .collect(Collectors.toList()) : List.of();

        List<FileSummary> finalProposalFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_FINAL_PROPOSAL,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> finalSubmissionFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_FINAL_SUBMISSION,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> relatedFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_PROPOSAL_RELATED,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> meetingNotesFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_PROPOSAL_MEETING_NOTES,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> structureDiagramFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_STRUCTURE_DIAGRAM,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        return TaskProposalResponse.from(
                proposal,
                writers,
                finalProposalFiles,
                finalSubmissionFiles,
                relatedFiles,
                meetingNotesFiles,
                structureDiagramFiles
        );
    }

    public TaskPresentationResponse getTaskPresentation(Long userId, Long taskId) {

        Task task = getTaskById(taskId);
        TaskPresentation presentation = taskPresentationRepository.findByTask(task).orElse(null);

        List<TaskMemberSummary> makers =
                presentation != null ? taskPresentationMakerRepository.findByTaskPresentation(presentation)
                        .stream()
                        .map(TaskMemberSummary::fromPresentationMaker)
                        .collect(Collectors.toList()) : List.of();

        List<FileSummary> finalPresentationFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_FINAL_PRESENTATION,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> draftPresentationFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_DRAFT_PRESENTATION,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        return TaskPresentationResponse.from(
                presentation,
                makers,
                finalPresentationFiles,
                draftPresentationFiles
        );
    }

    @Transactional
    public void updatePresentation(Long userId, Long taskId, TaskPresentationUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        TaskPresentation presentation = taskPresentationRepository.findByTask(task)
                .orElseGet(() -> TaskPresentation.builder().task(task).build());

        presentation.update(
                request.presentationDeadline(),
                request.presentationDate(),
                request.presentationLocation(),
                request.presenter(),
                request.attendeeLimit(),
                request.attendees()
        );

        taskPresentationRepository.save(presentation);

        taskPresentationMakerRepository.deleteByTaskPresentation(presentation);

        if (request.presentationMakerIds() != null) {
            for (Long makerId : request.presentationMakerIds()) {
                User maker = userService.findUserById(makerId);
                TaskPresentationMaker presentationMaker = TaskPresentationMaker.builder()
                        .taskPresentation(presentation)
                        .user(maker)
                        .build();
                taskPresentationMakerRepository.save(presentationMaker);
            }
        }
    }

    private Task getTaskById(Long taskId) {

        return taskRepository.findById(taskId).orElseThrow(() -> new ApiException(TaskErrorCode.TASK_NOT_FOUND));
    }
}
