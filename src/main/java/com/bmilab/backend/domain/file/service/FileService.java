package com.bmilab.backend.domain.file.service;

import com.bmilab.backend.domain.file.dto.request.UploadFileRequest;
import com.bmilab.backend.domain.file.dto.response.FilePresignedUrlResponse;
import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import com.bmilab.backend.global.external.s3.S3Service;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {
    private final S3Service s3Service;
    private final FileInformationRepository fileInformationRepository;

    public FilePresignedUrlResponse generatePresignedUrl(String fileKey, String contentType) {
        URL presignedUrl = s3Service.generatePresignedUploadUrl(fileKey, contentType, 10L);

        return new FilePresignedUrlResponse(presignedUrl.toString());
    }

    @Transactional
    public FileSummary uploadFile(UploadFileRequest request) {
        String fileKey = request.domainType().name().toLowerCase() + "/" + request.fileName();

        FileInformation fileInformation = FileInformation.builder()
                .name(request.fileName())
                .extension(request.extension())
                .domainType(request.domainType())
                .uploadUrl(s3Service.getUploadedFileUrl(fileKey))
                .build();

        fileInformationRepository.save(fileInformation);

        return FileSummary.from(fileInformation);
    }
}
