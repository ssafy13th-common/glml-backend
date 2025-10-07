package com.ssafy.a705.diary.presentation.dto.response;

import com.ssafy.a705.diary.domain.entity.Diary;
import java.time.LocalDate;
import java.util.List;

public record DiaryDetailRes(
        Long id,
        String location,
        LocalDate startedAt,
        LocalDate endedAt,
        String content,
        List<String> imageUrls) {

    public static DiaryDetailRes from(Diary diary, List<String> presignedUrls) {
        return new DiaryDetailRes(diary.getId(), diary.getLocation().getName(),
                diary.getStartedAt(),
                diary.getEndedAt(), diary.getContent(), presignedUrls);
    }
}