package com.bmilab.backend.domain.project.dto.request;

import java.time.LocalDate;

public record ProjectCompleteRequest(
        LocalDate endDate
) {
}
