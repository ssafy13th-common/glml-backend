package com.ssafy.a705.member.presentation.dto;

public record TokenRes(String accessToken, String refreshToken) {

    public static TokenRes of(String accessToken, String refreshToken) {
        return new TokenRes(accessToken, refreshToken);
    }
}
