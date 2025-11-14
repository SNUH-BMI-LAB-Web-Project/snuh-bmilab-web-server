package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.dto.query.ProjectMissingReportInfo;
import com.bmilab.backend.domain.report.dto.query.UserProjectMissingReportInfo;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.utils.ExcelRow;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    public String toBiWeeklyMissingReports(
            List<ProjectMissingReportInfo> projectsMissingReports,
            List<UserProjectMissingReportInfo> userProjectsMissingReports
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("*\\[프로젝트 별 미보고 현황\\]*\n");
        sb.append("_최근 2주 동안 한 번도 보고되지 않은 프로젝트입니다._\n\n");

        if (projectsMissingReports.isEmpty()) {
            sb.append("    해당 없음\n");
        } else {
            // 실무책임자별로 그룹핑 (가나다순)
            Map<String, List<ProjectMissingReportInfo>> groupedByLeader = projectsMissingReports.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.firstLeaderName() != null ? p.firstLeaderName() : "리더 미지정",
                            Collectors.toList()
                    ));

            // 실무책임자 가나다순 정렬
            groupedByLeader.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        String leaderName = entry.getKey();
                        List<ProjectMissingReportInfo> projects = entry.getValue();

                        sb.append("*\\[실무책임자: ").append(escMdV2(leaderName)).append("\\]*\n");

                        for (ProjectMissingReportInfo project : projects) {
                            sb.append("  • *프로젝트*: ").append(escMdV2(project.projectTitle())).append("\n");
                            sb.append("    _마지막 보고_: ");
                            if (project.lastReportDate() != null) {
                                sb.append("`").append(escCode(project.lastReportDate().toString())).append("`");
                            } else {
                                sb.append("보고 이력 없음");
                            }
                            sb.append("\n\n");
                        }
                    });
        }

        sb.append("\\-".repeat(30)).append("\n\n");

        sb.append("*\\[개인별 미보고 현황\\]*\n");
        sb.append("_최근 2주 동안 개인이 참여 프로젝트에 보고하지 않은 경우입니다._\n\n");

        if (userProjectsMissingReports.isEmpty()) {
            sb.append("    해당 없음\n");
        } else {
            // 유저별로 그룹핑 (유저 이름 가나다순)
            Map<Long, List<UserProjectMissingReportInfo>> groupedByUser = userProjectsMissingReports.stream()
                    .collect(Collectors.groupingBy(UserProjectMissingReportInfo::userId));

            groupedByUser.values().stream()
                    .sorted(Comparator.comparing(list -> list.get(0).userName()))
                    .forEach(userProjects -> {
                        UserProjectMissingReportInfo firstInfo = userProjects.get(0);
                        sb.append("*\\[이름: ").append(escMdV2(firstInfo.userName()))
                                .append(" \\| 이메일: ").append(escMdV2(firstInfo.userEmail()))
                                .append("\\]*\n");

                        for (UserProjectMissingReportInfo info : userProjects) {
                            sb.append("  • *미보고 프로젝트*: ").append(escMdV2(info.projectTitle())).append("\n");
                            sb.append("    _마지막 보고_: ");
                            if (info.lastReportDate() != null) {
                                sb.append("`").append(escCode(info.lastReportDate().toString())).append("`");
                            } else {
                                sb.append("보고 이력 없음");
                            }
                            sb.append("\n\n");
                        }
                    });
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