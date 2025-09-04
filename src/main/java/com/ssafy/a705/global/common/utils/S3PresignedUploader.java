package com.ssafy.a705.global.common.utils;

import com.ssafy.a705.global.common.exception.InvalidFileNameException;
import com.ssafy.a705.global.common.exception.UnsupportedTypeException;
import com.ssafy.a705.global.image.dto.response.PresignedUrlRes;
import com.ssafy.a705.global.image.exception.InvalidPresignedUrlPrefixException;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3PresignedUploader {

    private final S3Presigner s3Presigner;

    private static final String IMAGE_PREFIX = "image/";
    private static final int EXPIRATION_MINUTES = 5;
    private static final Set<String> SUPPORTED_IMAGE_TYPES = Set.of(
            "jpg", "jpeg", "png", "gif", "webp"
    );
    private static final Set<String> PRESIGNED_URL_PREFIXES = Set.of(
            "diaries", "trackings", "groups", "members", "receipts"
    );

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public PresignedUrlRes generatePresignedPutUrl(String domain,
            String fileName) {
        String contentType = getContentTypeFromFileName(fileName);
        checkValidDomain(domain);

        String key = domain + "/" + UUID.randomUUID() + "_"
                + fileName;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(b ->
                b.putObjectRequest(objectRequest)
                        .signatureDuration(Duration.ofMinutes(EXPIRATION_MINUTES)));

        return PresignedUrlRes.of(key, presignedRequest.url().toString());
    }

    public String generatePresignedGetUrl(String fileName) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(EXPIRATION_MINUTES))
                        .getObjectRequest(objectRequest).build());

        return presignedRequest.url().toString();
    }

    private static String getContentTypeFromFileName(String fileName) {
        String ext = Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.') + 1).toLowerCase())
                .orElseThrow(InvalidFileNameException::new);

        if (ext.equals("jpg")) {
            ext = "jpeg";
        }

        if (!SUPPORTED_IMAGE_TYPES.contains(ext)) {
            throw new UnsupportedTypeException();
        }

        return IMAGE_PREFIX + ext;
    }

    private static void checkValidDomain(String domain) {
        if (!PRESIGNED_URL_PREFIXES.contains(domain)) {
            throw new InvalidPresignedUrlPrefixException();
        }
    }


}
