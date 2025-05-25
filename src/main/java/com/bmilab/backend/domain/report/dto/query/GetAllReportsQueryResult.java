package com.bmilab.backend.domain.report.dto.query;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.report.entity.Report;
import java.util.List;

public record GetAllReportsQueryResult(
        Report report,
        List<FileInformation> files
) {
}
