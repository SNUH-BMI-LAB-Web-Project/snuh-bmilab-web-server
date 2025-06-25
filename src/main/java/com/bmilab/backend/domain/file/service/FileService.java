package com.bmilab.backend.domain.file.service;

import com.bmilab.backend.domain.file.dto.request.UploadFileRequest;
import com.bmilab.backend.domain.file.dto.response.FilePresignedUrlResponse;
import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.exception.FileErrorCode;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {
    private final S3Service s3Service;
    private final FileInformationRepository fileInformationRepository;

    public FilePresignedUrlResponse generatePresignedUrl(FileDomainType domainType, String fileName,
            String contentType) {
        UUID uuid = UUID.randomUUID();
        String fileKey = domainType.name().toLowerCase() + "/" + uuid + "_" + fileName;
        URL presignedUrl = s3Service.generatePresignedUploadUrl(fileKey, contentType, 10L);

        return new FilePresignedUrlResponse(uuid, presignedUrl.toString());
    }

    @Transactional
    public FileSummary uploadFile(UploadFileRequest request) {
        String fileKey = request.domainType().name().toLowerCase() + "/" + URLEncoder.encode(request.fileName(), StandardCharsets.UTF_8);

        FileInformation fileInformation = FileInformation.builder()
                .id(request.uuid())
                .name(request.fileName())
                .extension(request.extension())
                .domainType(request.domainType())
                .size(request.size())
                .uploadUrl(s3Service.getUploadedFileUrl(fileKey))
                .build();

        fileInformationRepository.save(fileInformation);

        return FileSummary.from(fileInformation);
    }

    @Transactional
    public void deleteFile(FileDomainType domainType, UUID fileId) {
        FileInformation fileInformation = fileInformationRepository.findById(fileId)
                .orElseThrow(() -> new ApiException(FileErrorCode.FILE_NOT_FOUND));

        if (!domainType.equals(fileInformation.getDomainType())) {
            throw new ApiException(FileErrorCode.FILE_DOMAIN_MISMATCH);
        }

        s3Service.deleteFile(fileInformation.getUploadUrl());
        fileInformationRepository.deleteById(fileId);
    }

    public void deleteAllFileByDomainTypeAndEntityId(FileDomainType domainType, Long entityId) {
        List<FileInformation> files = fileInformationRepository.findAllByDomainTypeAndEntityId(domainType, entityId);

        fileInformationRepository.deleteAll(files);
    }
}
