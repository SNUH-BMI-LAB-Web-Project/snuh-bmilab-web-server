package com.bmilab.backend.domain.project.event.listener;

import com.bmilab.backend.domain.project.dto.ExternalProfessorSummary;
import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.event.ProjectUpdateEvent;
import com.bmilab.backend.domain.project.repository.ExternalProfessorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProjectUpdateEventListener {
    private final ExternalProfessorRepository externalProfessorRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProjectUpdate(ProjectUpdateEvent event) {
        Project project = event.project();
        List<String> exProfessorStrings = new ArrayList<>();

        exProfessorStrings.addAll(project.getPIList());
        exProfessorStrings.addAll(project.getPracticalProfessorList());

        Set<ExternalProfessorSummary> externalProfessors = exProfessorStrings.stream()
                .map(ExternalProfessorSummary::from)
                .collect(Collectors.toSet());

        List<ExternalProfessorSummary> exists = externalProfessorRepository.findExists(externalProfessors);

        externalProfessors.removeAll(exists);

        externalProfessors.stream()
                .map(dto ->
                        ExternalProfessor.builder()
                                .name(dto.name())
                                .organization(dto.organization())
                                .department(dto.department())
                                .build()
                )
                .forEach(externalProfessorRepository::save);
    }
}
