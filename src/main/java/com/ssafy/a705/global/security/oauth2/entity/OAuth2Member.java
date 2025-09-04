package com.ssafy.a705.global.security.oauth2.entity;

import java.util.Map;

public abstract class OAuth2Member {

    protected Map<String, Object> attributes;

    public OAuth2Member(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); // 소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"

    public abstract String getNickname();

    public abstract String getImageUrl();

    public abstract String getEmail();
}
