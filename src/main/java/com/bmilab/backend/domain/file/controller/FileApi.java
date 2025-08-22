package com.bmilab.backend.domain.file.controller;

import com.bmilab.backend.domain.file.dto.request.UploadFileRequest;
import com.bmilab.backend.domain.file.dto.response.FilePresignedUrlResponse;
import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "File", description = "첨부파일 API")
public interface FileApi {
    @Operation(summary = "Presigned URL 발급", description = "AWS S3에 파일을 업로드하기 위한 Presigned URL을 발급받는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Presigned URL 발급 성공"
                    ),
            }
    )
    ResponseEntity<FilePresignedUrlResponse> generatePresignedUrl(
            @RequestParam String fileName,
            @RequestParam String contentType
    );

    @Operation(summary = "(S3 업로드 후) 첨부파일 정보 저장", description = "S3 업로드 후 DB에 첨부파일 정보를 저장하기 위한 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "파일 업로드 성공"
                    ),
            }
    )
    ResponseEntity<FileSummary> uploadFile(@RequestBody UploadFileRequest request);
}
