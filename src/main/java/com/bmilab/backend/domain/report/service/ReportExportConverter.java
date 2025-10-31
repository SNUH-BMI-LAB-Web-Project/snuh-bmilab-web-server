package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.utils.ExcelRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportExportConverter {

    public List<ExcelRow> toRows(List<GetAllReportsQueryResult> results) {
        return results.stream().map(result -> {
            Report report = result.report();
            List<FileInformation> files = result.files();

            String fileUrls = files.stream()
                    .map(FileInformation::getUploadUrl)
                    .collect(Collectors.joining("\n"));

            return ExcelRow.of(
                    report.getDate().toString(),
                    report.getUser().getName(),
                    report.getUser().getEmail(),
                    report.getProject().getTitle(),
                    report.getContent(),
                    fileUrls
            );
        }).toList();
    }

    public String toMailBodyPlain(List<GetAllReportsQueryResult> results) {
        StringBuilder sb = new StringBuilder();
        for (ExcelRow row : toRows(results)) {
            List<String> d = row.data();
            sb.append(d.get(0)).append(" / ").append(d.get(1)).append(" / ")
                    .append(d.get(2)).append(" / ").append(d.get(3)).append("\n")
                    .append(d.get(4)).append("\n");
            if (d.size() > 5 && d.get(5) != null && !d.get(5).isEmpty()) {
                for (String url : d.get(5).split("\\R")) {
                    if (!url.isBlank()) sb.append("첨부: ").append(url).append("\n");
                }
            }
            sb.append("\n")
                    .append("================================\n")
                    .append("================================\n")
                    .append("\n");
        }
        return sb.toString();
    }

    public String toTelegramBodyMarkdown(List<GetAllReportsQueryResult> results) {
        StringBuilder sb = new StringBuilder();
        for (ExcelRow row : toRows(results)) {
            List<String> d = row.data();
            sb.append("`").append(escCode(d.get(0))).append("`").append(" / ")
                    .append("*").append(escMdV2(d.get(1))).append("*").append(" / ")
                    .append("`").append(escCode(d.get(2))).append("`").append(" / ")
                    .append("_").append(escMdV2(d.get(3))).append("_").append("\n")
                    .append(escMdV2(d.get(4))).append("\n");
            if (d.size() > 5 && d.get(5) != null && !d.get(5).isEmpty()) {
                for (String url : d.get(5).split("\\R")) {
                    if (!url.isBlank()) sb.append("첨부: ")
                            .append("`").append(escCode(url)).append("`")
                            .append("\n");
                }
            }
            sb.append("\n");
            sb.append("\\=".repeat(20)).append("\n");
            sb.append("\\=".repeat(20)).append("\n");
            sb.append("\n");

        }
        return sb.toString();
    }

    public String toWeeklyMissingReports(
            List<Project> projectsWithoutReports,
            List<User> usersWithoutReports
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("\\[보고 안된 프로젝트\\]\n");
        if (projectsWithoutReports.isEmpty()) {
            sb.append("    \\-    없음\n");
        } else {
            for (Project project : projectsWithoutReports) {
                sb.append("    •    ");
                sb.append(escMdV2(project.getTitle()));
                sb.append("\n");
            }
        }

        sb.append("\n\\[보고 안한 유저\\]\n");
        if (usersWithoutReports.isEmpty()) {
            sb.append("    \\-    없음\n");
        } else {
            for (User user : usersWithoutReports) {
                sb.append("    •    ");
                sb.append(escMdV2(user.getName()));
                sb.append(" \\(");
                sb.append(escMdV2(user.getEmail()));
                sb.append("\\)\n");
            }
        }

        return sb.toString();
    }

    private static String escMdV2(String s) {
        if (s == null) return "";
        s = s.replace("\\", "\\\\");
        return s.replaceAll("([_\\*\\[\\]\\(\\)~`>#+\\-=|{}.!])", "\\\\$1");
    }
    private static String escCode(String s) {
        if (s == null) return "";
        s = s.replace("\\", "\\\\");
        return s.replace("`", "\\`");
    }

}