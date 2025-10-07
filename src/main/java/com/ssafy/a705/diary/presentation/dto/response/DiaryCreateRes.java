package com.ssafy.a705.diary.presentation.dto.response;

import com.ssafy.a705.diary.domain.entity.Diary;

public record DiaryCreateRes(Long id) {

    public static DiaryCreateRes from(Diary diary) {
        return new DiaryCreateRes(diary.getId());
    }
}
