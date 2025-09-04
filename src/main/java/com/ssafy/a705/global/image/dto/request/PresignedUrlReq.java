package com.ssafy.a705.global.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PresignedUrlReq(String domain,
                              @NotBlank(message = "파일 이름이 입력되지 않았습니다.")
                              @Pattern(regexp = "^[a-zA-Z0-9_-]+\\.(jpg|jpeg|png|gif|webp)$", message = "파일 이름 형식이 잘못 되었습니다.")
                              String fileName) {

}