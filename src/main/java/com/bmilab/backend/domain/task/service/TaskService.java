package com.bmilab.backend.domain.task.service;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.task.dto.request.TaskRequest;
import com.bmilab.backend.domain.task.dto.response.TaskStatsResponse;
import com.bmilab.backend.domain.task.dto.response.TaskSummaryResponse;
import com.bmilab.backend.domain.task.entity.Task;
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
    private final UserService userService;


    @Transactional
    public void createTask(Long userId, TaskRequest request) {
        if (request.researchTaskNumber() != null &&
                taskRepository.existsByResearchTaskNumber(request.researchTaskNumber())) {
            throw new ApiException(TaskErrorCode.TASK_DUPLICATE_RESEARCH_NUMBER);
        }

        User practicalManager = request.practicalManagerId() != null
                ? userService.findUserById(request.practicalManagerId())
                : null;

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

        User practicalManager = request.practicalManagerId() != null
                ? userService.findUserById(request.practicalManagerId())
                : null;

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

        return new TaskStatsResponse(
                totalCount,
                inProgressCount,
                proposalWritingCount,
                presentationPreparingCount
        );
    }

    public Page<TaskSummaryResponse> getAllTasks(Long userId, TaskStatus status, String keyword, Pageable pageable) {
        Page<Task> tasks = taskRepository.findTasksForList(status, keyword, pageable);
        return tasks.map(TaskSummaryResponse::from);
    }

    private Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ApiException(TaskErrorCode.TASK_NOT_FOUND));
    }
}
