package com.ssafy.a705.domain.member._auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfileRes(
        Long id,
        Properties properties,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
    public String email(){
        return kakaoAccount != null ? kakaoAccount().email() : null;
    }

    public String nickname(){
        if (kakaoAccount != null && kakaoAccount.profile != null && kakaoAccount.profile.nickname != null) {
            return kakaoAccount.profile.nickname;
        }
        return properties != null ? properties.nickname : null;
    }

    public String profileImage() {
        if (kakaoAccount != null && kakaoAccount.profile != null && kakaoAccount.profile.profile_image_url != null) {
            return kakaoAccount.profile.profile_image_url;
        }
        return properties != null ? properties.profileImage : null;
    }

    // 프로필 정보 요청 시 응답 형태와 맞추기 위함
    public static record Properties(
            String nickname,
            @JsonProperty("profile_image") String profileImage,
            @JsonProperty("thumbnail_image") String thumbnailImage
    ) {}

    public static record KakaoAccount(
            boolean has_email,
            @JsonProperty("email_needs_agreement") Boolean email_needs_agreement,
            @JsonProperty("is_email_valid") Boolean is_email_valid,
            @JsonProperty("is_email_verified") Boolean is_email_verified,
            String email,
            Profile profile
    ){
        public static record Profile(
                String nickname,
                @JsonProperty("thumbnail_image_url") String thumbnail_image_url,
                @JsonProperty("profile_image_url") String profile_image_url,
                @JsonProperty("is_default_image") Boolean is_default_image,
                @JsonProperty("is_default_nickname") Boolean is_default_nickname
        ) {}
    }
}
