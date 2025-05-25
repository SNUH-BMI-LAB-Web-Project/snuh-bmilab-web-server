package com.bmilab.backend.domain.project.dto.query;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.entity.Timeline;
import java.util.List;

public record GetAllTimelinesQueryResult(
        Timeline timeline,
        List<FileInformation> files
) {
}
