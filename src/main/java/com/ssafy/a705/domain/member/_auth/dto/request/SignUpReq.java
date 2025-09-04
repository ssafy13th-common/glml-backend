package com.ssafy.a705.domain.member._auth.dto.request;

import com.ssafy.a705.domain.member.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record SignUpReq(
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(min = 2, max = 20) String nickname,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 6, max = 20) String password,
        @NonNull Gender gender,
        String profileImage
) {

}
