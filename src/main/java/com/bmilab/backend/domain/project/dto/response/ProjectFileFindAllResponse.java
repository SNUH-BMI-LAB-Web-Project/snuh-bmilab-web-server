package com.bmilab.backend.domain.project.dto.response;

import java.util.List;

public record ProjectFileFindAllResponse(
        List<ProjectFileSummary> files
) {

}
