package com.bmilab.backend.domain.file.controller;

import com.bmilab.backend.domain.file.dto.request.UploadFileRequest;
import com.bmilab.backend.domain.file.dto.response.FilePresignedUrlResponse;
import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController implements FileApi {

    private final FileService fileService;

    @GetMapping("/presigned-url")
    public ResponseEntity<FilePresignedUrlResponse> generatePresignedUrl(
            @RequestParam String fileName,
            @RequestParam String contentType
    ) {

        return ResponseEntity.ok(fileService.generatePresignedUrl(
                URLDecoder.decode(fileName, StandardCharsets.UTF_8),
                URLDecoder.decode(contentType, StandardCharsets.UTF_8)
        ));
    }

    @PostMapping
    public ResponseEntity<FileSummary> uploadFile(@RequestBody @Valid UploadFileRequest request) {

        return ResponseEntity.ok(fileService.uploadFile(request));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID fileId) {

        fileService.deleteFile(fileId);
        return ResponseEntity.ok().build();
    }
}
