package com.ssafy.a705.global.image.dto.response;

public record PresignedUrlRes(String fileName, String presignedUrl) {

    public static PresignedUrlRes of(String fileName, String presignedUrl) {
        return new PresignedUrlRes(fileName, presignedUrl);
    }
}
