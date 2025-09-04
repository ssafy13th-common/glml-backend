package com.ssafy.a705.domain.location.dto.response;

public record LocationInfoRes(
        String adminName,
        String sidoName,
        String adminCode
) {

    public static LocationInfoRes of(String adminName, String sidoName, String adminCode) {
        return new LocationInfoRes(adminName, sidoName, adminCode);
    }

}
