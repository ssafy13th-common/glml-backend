package com.ssafy.a705.global.image.controller;

import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
import com.ssafy.a705.global.image.dto.request.PresignedUrlsReq;
import com.ssafy.a705.global.image.dto.response.PresignedUrlRes;
import com.ssafy.a705.global.image.dto.response.PresignedUrlsRes;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/images/presigned-urls")
public class PresignedUrlRestController {

    private final S3PresignedUploader s3PresignedUploader;

    @PostMapping
    public ResponseEntity<ApiResponse<PresignedUrlsRes>> getPresignedUrlsForImages(
            @RequestBody @Valid PresignedUrlsReq presignedUrlReq) {
        List<PresignedUrlRes> presignedUrls = presignedUrlReq.fileNames().stream()
                .map(fileName ->
                        s3PresignedUploader.generatePresignedPutUrl(
                                presignedUrlReq.domain(),
                                fileName)).toList();
        PresignedUrlsRes res = PresignedUrlsRes.of(presignedUrls);
        return ApiResponse.ok(res);
    }
}
