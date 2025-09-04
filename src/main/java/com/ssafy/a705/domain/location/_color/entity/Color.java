package com.ssafy.a705.domain.location._color.entity;

import lombok.Getter;

@Getter
public enum Color {
    SEOUL(11, "서울특별시", "E74C3C"),
    BUSAN(26, "부산광역시", "2980B9"),
    DAEGU(27, "대구광역시", "E74C3C"),
    INCHEON(28, "인천광역시", "1ABC9C"),
    GWANGJU(29, "광주광역시", "E91E63"),
    DAEJEON(30, "대전광역시", "F1C40F"),
    ULSAN(31, "울산광역시", "5DADE2"),
    SEJONG(36, "세종특별자치시", "2ECC71"),
    GYEONGGI(41, "경기도", "F39C12"),
    CHUNGCHEONGBUK(43, "충청북도", "45B39D"),
    CHUNGCHEONGNAM(44, "충청남도", "45B39D"),
    JEOLLABUK(45, "전라북도", "5DADE2"),
    JEOLLANAM(46, "전라남도", "5DADE2"),
    GYEONGSANGBUK(47, "경상북도", "9B59B6"),
    GYEONGSANGNAM(48, "경상남도", "9B59B6"),
    JEJU(50, "제주특별자치도", "E67E22"),
    GANGWON(51, "강원특별자치도", "27AE60");

    private final Integer code;
    private final String korName;
    private final String hexColor;

    Color(Integer code, String korName, String hexColor) {
        this.code = code;
        this.korName = korName;
        this.hexColor = hexColor;
    }

    public static Color fromCode(Integer code) {
        for (Color color : values()) {
            if (color.code.equals(code)) {
                return color;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
