package com.ssafy.a705.domain.diary.dto.response;

import com.ssafy.a705.domain.diary.entity.Diary;

public record DiaryCreateRes(Long id) {

    public static DiaryCreateRes from(Diary diary) {
        return new DiaryCreateRes(diary.getId());
    }
}
