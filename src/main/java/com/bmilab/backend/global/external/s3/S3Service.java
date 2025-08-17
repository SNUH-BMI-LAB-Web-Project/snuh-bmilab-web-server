package com.bmilab.backend.global.external.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.exception.GlobalErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public String uploadFile(MultipartFile file, String newFileName) {
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String fileNameByProfile = (activeProfile.equals("dev")) ? "dev/" + newFileName : newFileName;
        String changedName = fileNameByProfile + ext;

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
        String fileKey = getS3Key(fileUrl);

        if (!amazonS3.doesObjectExist(bucket, fileKey)) {
            throw new ApiException(GlobalErrorCode.FILE_NOT_FOUND);
        }

        try {
            amazonS3.deleteObject(bucket, fileKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void moveFileDirectory(String fileUrl, String previousDirectory, String newDirectory) {
        String fileKey = getS3Key(fileUrl);
        String newFileKey = fileKey.replace(previousDirectory + "/", newDirectory + "/");
        amazonS3.copyObject(new CopyObjectRequest(bucket, fileKey, bucket, newFileKey));
        amazonS3.deleteObject(bucket, fileKey);
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

    public String getS3Key(String uploadedUrl) {
        return URLDecoder.decode(uploadedUrl.replace(baseUrl, ""), StandardCharsets.UTF_8);
    }

    public StreamingResponseBody downloadS3FilesByZip(List<String> fileKeys) {
        return outputStream -> {
            try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
                for (String key : fileKeys) {
                    S3Object s3Object = amazonS3.getObject(bucket, key);
                    try (InputStream s3In = s3Object.getObjectContent()) {
                        zipOut.putNextEntry(new ZipEntry(key));

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = s3In.read(buffer)) != -1) {
                            zipOut.write(buffer, 0, bytesRead);
                        }

                        zipOut.closeEntry();
                    }
                }
                zipOut.finish();
            }
        };
    }

}
