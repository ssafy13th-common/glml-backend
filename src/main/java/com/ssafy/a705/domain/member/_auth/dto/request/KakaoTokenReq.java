package com.ssafy.a705.domain.member._auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.a705.domain.member.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record KakaoTokenReq(
        @JsonProperty("accessToken") String accessToken,
        @NotBlank(message = "이름이 입력되지 않았습니다.") String name,
        @NonNull Gender gender
) {

}
