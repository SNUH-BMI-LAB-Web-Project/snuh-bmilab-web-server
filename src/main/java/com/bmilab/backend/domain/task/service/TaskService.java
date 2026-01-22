package com.bmilab.backend.domain.task.service;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.exception.ProjectErrorCode;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.research.paper.dto.response.PaperSummaryResponse;
import com.bmilab.backend.domain.research.paper.entity.Paper;
import com.bmilab.backend.domain.research.paper.entity.PaperAuthor;
import com.bmilab.backend.domain.research.paper.entity.PaperCorrespondingAuthor;
import com.bmilab.backend.domain.research.paper.repository.PaperAuthorRepository;
import com.bmilab.backend.domain.research.paper.repository.PaperCorrespondingAuthorRepository;
import com.bmilab.backend.domain.research.paper.repository.PaperRepository;
import com.bmilab.backend.domain.research.patent.dto.response.PatentSummaryResponse;
import com.bmilab.backend.domain.research.patent.entity.Patent;
import com.bmilab.backend.domain.research.patent.entity.PatentAuthor;
import com.bmilab.backend.domain.research.patent.repository.PatentAuthorRepository;
import com.bmilab.backend.domain.research.patent.repository.PatentRepository;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationSummaryResponse;
import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentationAuthor;
import com.bmilab.backend.domain.research.presentation.repository.AcademicPresentationAuthorRepository;
import com.bmilab.backend.domain.research.presentation.repository.AcademicPresentationRepository;
import com.bmilab.backend.domain.task.dto.request.AcknowledgementUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskAgreementUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskBasicInfoUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPeriodRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPeriodUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPresentationUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskProposalUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskRequest;
import com.bmilab.backend.domain.task.dto.response.AcknowledgementResponse;
import com.bmilab.backend.domain.task.dto.response.TaskAgreementResponse;
import com.bmilab.backend.domain.task.dto.response.TaskBasicInfoResponse;
import com.bmilab.backend.domain.task.dto.response.TaskMemberSummary;
import com.bmilab.backend.domain.task.dto.response.TaskPeriodResponse;
import com.bmilab.backend.domain.task.dto.response.TaskPeriodSummaryResponse;
import com.bmilab.backend.domain.task.dto.response.TaskPresentationResponse;
import com.bmilab.backend.domain.task.dto.response.TaskProjectSummary;
import com.bmilab.backend.domain.task.dto.response.TaskProposalResponse;
import com.bmilab.backend.domain.task.dto.response.TaskStatsResponse;
import com.bmilab.backend.domain.task.dto.response.TaskSummaryResponse;
import com.bmilab.backend.domain.task.entity.*;
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

import java.time.LocalDate;
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
    private final AcknowledgementRepository acknowledgementRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final FileService fileService;
    private final PaperRepository paperRepository;
    private final PaperAuthorRepository paperAuthorRepository;
    private final PaperCorrespondingAuthorRepository paperCorrespondingAuthorRepository;
    private final AcademicPresentationRepository academicPresentationRepository;
    private final AcademicPresentationAuthorRepository academicPresentationAuthorRepository;
    private final PatentRepository patentRepository;
    private final PatentAuthorRepository patentAuthorRepository;


    @Transactional
    public void createTask(Long userId, TaskRequest request) {

        if (request.researchTaskNumber() != null &&
                taskRepository.existsByResearchTaskNumber(request.researchTaskNumber())) {
            throw new ApiException(TaskErrorCode.TASK_DUPLICATE_RESEARCH_NUMBER);
        }

        if (request.currentYear() > request.totalYears()) {
            throw new ApiException(TaskErrorCode.TASK_INVALID_YEAR);
        }

        User practicalManager =
                request.practicalManagerId() != null ? userService.findUserById(request.practicalManagerId()) : null;

        LocalDate taskStartDate = null;
        LocalDate taskEndDate = null;

        if (request.periods() != null && !request.periods().isEmpty()) {
            taskStartDate = request.periods().get(0).startDate();
            taskEndDate = request.periods().get(request.periods().size() - 1).endDate();
        }

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
                .startDate(taskStartDate)
                .endDate(taskEndDate)
                .totalYears(request.totalYears())
                .currentYear(request.currentYear())
                .status(request.status() != null ? request.status() : TaskStatus.PROPOSAL_WRITING)
                .isInternal(request.isInternal())
                .build();

        taskRepository.save(task);

        if (request.periods() != null && !request.periods().isEmpty()) {
            for (TaskPeriodRequest periodRequest : request.periods()) {
                TaskPeriod period = TaskPeriod.builder()
                        .task(task)
                        .yearNumber(periodRequest.yearNumber())
                        .startDate(periodRequest.startDate())
                        .endDate(periodRequest.endDate())
                        .build();
                taskPeriodRepository.save(period);
            }
        } else {
            for (int i = 1; i <= request.totalYears(); i++) {
                TaskPeriod period = TaskPeriod.builder()
                        .task(task)
                        .yearNumber(i)
                        .build();
                taskPeriodRepository.save(period);
            }
        }
    }

    @Transactional
    public void updateTask(Long userId, boolean isAdmin, Long taskId, TaskRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        if (request.currentYear() > request.totalYears()) {
            throw new ApiException(TaskErrorCode.TASK_INVALID_YEAR);
        }

        User practicalManager =
                request.practicalManagerId() != null ? userService.findUserById(request.practicalManagerId()) : null;

        LocalDate taskStartDate = null;
        LocalDate taskEndDate = null;

        if (request.periods() != null && !request.periods().isEmpty()) {
            taskStartDate = request.periods().get(0).startDate();
            taskEndDate = request.periods().get(request.periods().size() - 1).endDate();
        }

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
                taskStartDate,
                taskEndDate,
                request.totalYears(),
                request.currentYear(),
                request.leadInstitution(),
                request.leadProfessor(),
                request.snuhPi(),
                request.professorRole(),
                practicalManager,
                request.participatingInstitutions(),
                request.isInternal()
        );

        List<TaskPeriod> existingPeriods = taskPeriodRepository.findByTaskOrderByYearNumberAsc(task);
        taskPeriodRepository.deleteAll(existingPeriods);

        if (request.periods() != null && !request.periods().isEmpty()) {
            for (TaskPeriodRequest periodRequest : request.periods()) {
                TaskPeriod period = TaskPeriod.builder()
                        .task(task)
                        .yearNumber(periodRequest.yearNumber())
                        .startDate(periodRequest.startDate())
                        .endDate(periodRequest.endDate())
                        .build();
                taskPeriodRepository.save(period);
            }
        } else {
            for (int i = 1; i <= request.totalYears(); i++) {
                TaskPeriod period = TaskPeriod.builder()
                        .task(task)
                        .yearNumber(i)
                        .build();
                taskPeriodRepository.save(period);
            }
        }
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
        return tasks.map(task -> {
            List<TaskPeriodSummaryResponse> periods = taskPeriodRepository.findByTaskOrderByYearNumberAsc(task)
                    .stream()
                    .map(TaskPeriodSummaryResponse::from)
                    .collect(Collectors.toList());
            return TaskSummaryResponse.from(task, periods);
        });
    }

    public TaskSummaryResponse getTask(Long userId, Long taskId) {

        Task task = getTaskById(taskId);
        List<TaskPeriodSummaryResponse> periods = taskPeriodRepository.findByTaskOrderByYearNumberAsc(task)
                .stream()
                .map(TaskPeriodSummaryResponse::from)
                .collect(Collectors.toList());
        return TaskSummaryResponse.from(task, periods);
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

        List<TaskPeriodResponse> periods = taskPeriodRepository.findByTaskOrderByYearNumberAsc(task)
                .stream()
                .map(period -> {
                    List<FileSummary> periodFiles = fileService.findAllByDomainTypeAndEntityId(
                                    FileDomainType.TASK_PERIOD_FILES, period.getId())
                            .stream()
                            .map(FileSummary::from)
                            .collect(Collectors.toList());

                    List<FileSummary> interimReportFiles = fileService.findAllByDomainTypeAndEntityId(
                                    FileDomainType.TASK_PERIOD_INTERIM_REPORT, period.getId())
                            .stream()
                            .map(FileSummary::from)
                            .collect(Collectors.toList());

                    List<FileSummary> annualReportFiles = fileService.findAllByDomainTypeAndEntityId(
                                    FileDomainType.TASK_PERIOD_ANNUAL_REPORT, period.getId())
                            .stream()
                            .map(FileSummary::from)
                            .collect(Collectors.toList());

                    return TaskPeriodResponse.from(period, periodFiles, interimReportFiles, annualReportFiles);
                })
                .collect(Collectors.toList());

        return TaskBasicInfoResponse.from(
                basicInfo,
                task.getStartDate(),
                task.getEndDate(),
                task.getThreeFiveRule(),
                rfpFiles,
                announcementFiles,
                periods
        );
    }

    @Transactional
    public void updateBasicInfo(Long userId, boolean isAdmin, Long taskId, TaskBasicInfoUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
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
                    task.getParticipatingInstitutions(),
                    task.getIsInternal()
            );
        }

        taskBasicInfoRepository.save(basicInfo);

        syncFiles(taskId, request.rfpFileIds(), FileDomainType.TASK_RFP);
        syncFiles(taskId, request.announcementFileIds(), FileDomainType.TASK_ANNOUNCEMENT);
    }

    @Transactional
    public void updateProposal(Long userId, boolean isAdmin, Long taskId, TaskProposalUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
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

        syncFiles(taskId, request.finalProposalFileIds(), FileDomainType.TASK_FINAL_PROPOSAL);
        syncFiles(taskId, request.finalSubmissionFileIds(), FileDomainType.TASK_FINAL_SUBMISSION);
        syncFiles(taskId, request.relatedFileIds(), FileDomainType.TASK_PROPOSAL_RELATED);
        syncFiles(taskId, request.meetingNotesFileIds(), FileDomainType.TASK_PROPOSAL_MEETING_NOTES);
        syncFiles(taskId, request.structureDiagramFileIds(), FileDomainType.TASK_STRUCTURE_DIAGRAM);

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

        List<TaskMemberSummary> writers = proposal != null ? taskProposalWriterRepository.findByTaskProposal(proposal)
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

        return TaskPresentationResponse.from(presentation, makers, finalPresentationFiles, draftPresentationFiles);
    }

    @Transactional
    public void updatePresentation(Long userId, boolean isAdmin, Long taskId, TaskPresentationUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
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

        syncFiles(taskId, request.finalPresentationFileIds(), FileDomainType.TASK_FINAL_PRESENTATION);
        syncFiles(taskId, request.draftPresentationFileIds(), FileDomainType.TASK_DRAFT_PRESENTATION);

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

    public TaskAgreementResponse getTaskAgreement(Long userId, Long taskId) {

        Task task = getTaskById(taskId);
        TaskAgreement agreement = taskAgreementRepository.findByTask(task).orElse(null);

        List<FileSummary> agreementFinalProposalFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_AGREEMENT_FINAL_PROPOSAL,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> agreementFinalSubmissionFiles = fileService.findAllByDomainTypeAndEntityId(
                        FileDomainType.TASK_AGREEMENT_FINAL_SUBMISSION,
                        taskId
                )
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        return TaskAgreementResponse.from(agreement, agreementFinalProposalFiles, agreementFinalSubmissionFiles);
    }

    @Transactional
    public void updateAgreement(Long userId, boolean isAdmin, Long taskId, TaskAgreementUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        TaskAgreement agreement = taskAgreementRepository.findByTask(task)
                .orElseGet(() -> TaskAgreement.builder().task(task).build());

        agreement.update(request.agreementDate());

        taskAgreementRepository.save(agreement);

        syncFiles(taskId, request.agreementFinalProposalFileIds(), FileDomainType.TASK_AGREEMENT_FINAL_PROPOSAL);
        syncFiles(taskId, request.agreementFinalSubmissionFileIds(), FileDomainType.TASK_AGREEMENT_FINAL_SUBMISSION);
    }

    public TaskPeriodResponse getTaskPeriod(Long userId, Long taskId, Long periodId) {

        Task task = getTaskById(taskId);

        TaskPeriod period = taskPeriodRepository.findById(periodId)
                .orElseThrow(() -> new ApiException(TaskErrorCode.TASK_NOT_FOUND));

        List<FileSummary> periodFiles = fileService.findAllByDomainTypeAndEntityId(FileDomainType.TASK_PERIOD_FILES, periodId)
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> interimReportFiles = fileService.findAllByDomainTypeAndEntityId(FileDomainType.TASK_PERIOD_INTERIM_REPORT, periodId)
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        List<FileSummary> annualReportFiles = fileService.findAllByDomainTypeAndEntityId(FileDomainType.TASK_PERIOD_ANNUAL_REPORT, periodId)
                .stream()
                .map(FileSummary::from)
                .collect(Collectors.toList());

        return TaskPeriodResponse.from(period, periodFiles, interimReportFiles, annualReportFiles);
    }

    @Transactional
    public void updateTaskPeriod(Long userId, boolean isAdmin, Long taskId, Long periodId, TaskPeriodUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        TaskPeriod period = taskPeriodRepository.findById(periodId)
                .orElseThrow(() -> new ApiException(TaskErrorCode.TASK_NOT_FOUND));

        User manager = request.managerId() != null ? userService.findUserById(request.managerId()) : null;

        List<User> members = request.memberIds() != null
                ? request.memberIds().stream().map(userService::findUserById).toList()
                : List.of();

        period.update(manager, members, request.startDate(), request.endDate());

        taskPeriodRepository.save(period);

        syncFiles(periodId, request.periodFileIds(), FileDomainType.TASK_PERIOD_FILES);
        syncFiles(periodId, request.interimReportFileIds(), FileDomainType.TASK_PERIOD_INTERIM_REPORT);
        syncFiles(periodId, request.annualReportFileIds(), FileDomainType.TASK_PERIOD_ANNUAL_REPORT);
    }

    @Transactional
    public void deleteTask(Long userId, boolean isAdmin, Long taskId) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        taskRepository.delete(task);
    }

    @Transactional
    public void deleteTaskFile(Long userId, boolean isAdmin, Long taskId, java.util.UUID fileId) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        fileService.deleteFile(fileId);
    }

    public AcknowledgementResponse getAcknowledgement(Long userId, Long taskId) {

        Task task = getTaskById(taskId);
        Acknowledgement acknowledgement = acknowledgementRepository.findByTask(task).orElse(null);

        return AcknowledgementResponse.from(acknowledgement);
    }

    @Transactional
    public void saveAcknowledgement(Long userId, boolean isAdmin, Long taskId, AcknowledgementUpdateRequest request) {

        Task task = getTaskById(taskId);

        if (!task.canBeEditedByUser(userId, isAdmin)) {
            throw new ApiException(TaskErrorCode.TASK_CANNOT_EDIT);
        }

        Acknowledgement acknowledgement = acknowledgementRepository.findByTask(task)
                .orElseGet(() -> Acknowledgement.builder().task(task).build());

        acknowledgement.update(request.acknowledgementText(), request.relatedInfo(), request.relatedLink());

        acknowledgementRepository.save(acknowledgement);
    }

    public List<TaskProjectSummary> getTaskProjects(Long userId, Long taskId) {

        Task task = getTaskById(taskId);
        List<Project> projects = projectRepository.findByTask(task);

        return projects.stream().map(TaskProjectSummary::from).collect(Collectors.toList());
    }

    @Transactional
    public void addProjectToTask(Long userId, Long taskId, Long projectId) {
        Task task = getTaskById(taskId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        // Project의 task 필드를 설정하여 연결
        project.setTask(task);
        projectRepository.save(project);
    }

    @Transactional
    public void removeProjectFromTask(Long userId, Long taskId, Long projectId) {
        Task task = getTaskById(taskId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

        // Project의 task 필드를 null로 설정하여 연결 해제
        project.setTask(null);
        projectRepository.save(project);
    }

    public List<PaperSummaryResponse> getTaskPapers(Long userId, Long taskId) {
        Task task = getTaskById(taskId);
        List<Paper> papers = paperRepository.findAllByTaskId(taskId);

        return papers.stream()
                .map(paper -> {
                    List<PaperCorrespondingAuthor> correspondingAuthors =
                            paperCorrespondingAuthorRepository.findAllByPaperId(paper.getId());
                    List<PaperAuthor> paperAuthors =
                            paperAuthorRepository.findAllByPaperId(paper.getId());
                    List<FileSummary> files = fileService.findAllByDomainTypeAndEntityId(
                                    FileDomainType.PAPER_ATTACHMENT, paper.getId())
                            .stream()
                            .map(FileSummary::from)
                            .toList();
                    return PaperSummaryResponse.from(paper, correspondingAuthors, paperAuthors, files);
                })
                .toList();
    }

    public List<AcademicPresentationSummaryResponse> getTaskPresentations(Long userId, Long taskId) {
        Task task = getTaskById(taskId);
        List<AcademicPresentation> presentations = academicPresentationRepository.findAllByTaskId(taskId);

        return presentations.stream()
                .map(presentation -> {
                    List<AcademicPresentationAuthor> authors =
                            academicPresentationAuthorRepository.findAllByAcademicPresentationId(presentation.getId());
                    return AcademicPresentationSummaryResponse.from(presentation, authors);
                })
                .toList();
    }

    public List<PatentSummaryResponse> getTaskPatents(Long userId, Long taskId) {
        Task task = getTaskById(taskId);
        List<Patent> patents = patentRepository.findAllByTaskId(taskId);

        return patents.stream()
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
    }

    private Task getTaskById(Long taskId) {

        return taskRepository.findById(taskId).orElseThrow(() -> new ApiException(TaskErrorCode.TASK_NOT_FOUND));
    }

    private void syncFiles(Long entityId, List<java.util.UUID> fileIds, FileDomainType domainType) {
        if (fileIds != null && !fileIds.isEmpty()) {
            fileService.syncFiles(fileIds, domainType, entityId);
        }
    }
}
