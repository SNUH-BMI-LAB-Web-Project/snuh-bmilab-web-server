package com.bmilab.backend.domain.file.controller;

import com.bmilab.backend.domain.file.dto.request.UploadFileRequest;
import com.bmilab.backend.domain.file.dto.response.FilePresignedUrlResponse;
import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController implements FileApi
{
    private final FileService fileService;

    @GetMapping("/presigned-url")
    public ResponseEntity<FilePresignedUrlResponse> generatePresignedUrl(
            @RequestParam FileDomainType domainType,
            @RequestParam String fileName,
            @RequestParam String contentType
    ) {
        String fileKey = domainType.name().toLowerCase() + "/" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        return ResponseEntity.ok(fileService.generatePresignedUrl(fileKey, contentType));
    }

    @PostMapping
    public ResponseEntity<FileSummary> uploadFile(@RequestBody UploadFileRequest request) {
        return ResponseEntity.ok(fileService.uploadFile(request));
    }
}
