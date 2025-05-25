package com.bmilab.backend.domain.project.dto.query;

import com.bmilab.backend.domain.project.entity.Timeline;
import com.bmilab.backend.domain.project.entity.TimelineFile;
import java.util.List;

public record GetAllTimelinesQueryResult(
        Timeline timeline,
        List<TimelineFile> timelineFiles
) {

}
