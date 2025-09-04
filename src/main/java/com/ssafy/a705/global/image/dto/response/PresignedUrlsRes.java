package com.ssafy.a705.global.image.dto.response;

import java.util.List;

public record PresignedUrlsRes(List<PresignedUrlRes> presignedUrls) {

    public static PresignedUrlsRes of(List<PresignedUrlRes> presignedUrlRes) {
        return new PresignedUrlsRes(presignedUrlRes);
    }
}