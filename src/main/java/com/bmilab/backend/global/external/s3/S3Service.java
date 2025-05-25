package com.bmilab.backend.global.external.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.exception.GlobalErrorCode;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    public String uploadFile(MultipartFile file, String newFileName) {
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String changedName = newFileName + ext;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(
                    new PutObjectRequest(bucket, changedName, file.getInputStream(), metadata)
            );
        } catch (IOException e) {
            throw new ApiException(GlobalErrorCode.FILE_UPLOAD_FAILED, e);
        }

        return amazonS3.getUrl(bucket, changedName).toString();
    }

    public void deleteFile(String fileUrl) {
        String imageKey = fileUrl.replace(baseUrl, "");

        if (!amazonS3.doesObjectExist(bucket, imageKey)) {
            throw new ApiException(GlobalErrorCode.FILE_NOT_FOUND);
        }

        try {
            amazonS3.deleteObject(bucket, imageKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public URL generatePresignedUploadUrl(String key, String contentType, long expirationInMinutes) {
        Date expiration = new Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        request.addRequestParameter("Content-Type", contentType);

        return amazonS3.generatePresignedUrl(request);
    }

    public String getUploadedFileUrl(String key) {
        return baseUrl + key;
    }
}
