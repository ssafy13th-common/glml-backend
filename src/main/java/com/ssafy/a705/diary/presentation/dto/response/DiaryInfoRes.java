package com.ssafy.a705.diary.presentation.dto.response;

import com.ssafy.a705.diary.domain.entity.Diary;
import java.time.LocalDate;
import java.util.Objects;

public record DiaryInfoRes(
        Long id,
        String location,
        LocalDate startedAt,
        LocalDate endedAt,
        String summary,
        String thumbnailUrl) {

    public static DiaryInfoRes from(Diary diary, String presignedUrl) {
        return new DiaryInfoRes(diary.getId(), diary.getLocation().getName(),
                diary.getStartedAt(),
                diary.getEndedAt(),
                Objects.isNull(diary.getContent()) ? "" : diary.getContent()
                        .substring(0, Math.min(diary.getContent().length(), 100)),
                presignedUrl);
    }
}