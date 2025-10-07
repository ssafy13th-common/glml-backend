package com.ssafy.a705.diary.presentation.dto.response;

import java.util.List;

public record DiaryInfosRes(List<DiaryInfoRes> diaries) {

    public static DiaryInfosRes from(List<DiaryInfoRes> diaries) {
        return new DiaryInfosRes(diaries);
    }
}