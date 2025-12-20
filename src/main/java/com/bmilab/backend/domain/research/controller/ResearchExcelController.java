package com.bmilab.backend.domain.research.controller;

import com.bmilab.backend.domain.research.service.ResearchExcelService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/research")
@RequiredArgsConstructor
public class ResearchExcelController implements ResearchExcelApi {

    private final ResearchExcelService researchExcelService;

    @GetMapping("/papers/excel")
    public ResponseEntity<byte[]> downloadPapersExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {
        byte[] bytes = researchExcelService.exportPapersToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"papers.xlsx\"")
                .body(bytes);
    }

    @GetMapping("/patents/excel")
    public ResponseEntity<byte[]> downloadPatentsExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {
        byte[] bytes = researchExcelService.exportPatentsToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"patents.xlsx\"")
                .body(bytes);
    }

    @GetMapping("/authors/excel")
    public ResponseEntity<byte[]> downloadPublicationsExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {
        byte[] bytes = researchExcelService.exportPublicationsToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"authors.xlsx\"")
                .body(bytes);
    }

    @GetMapping("/academic-presentations/excel")
    public ResponseEntity<byte[]> downloadAcademicPresentationsExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {
        byte[] bytes = researchExcelService.exportAcademicPresentationsToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"academic-presentations.xlsx\"")
                .body(bytes);
    }

    @GetMapping("/awards/excel")
    public ResponseEntity<byte[]> downloadAwardsExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {
        byte[] bytes = researchExcelService.exportAwardsToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"awards.xlsx\"")
                .body(bytes);
    }
}
